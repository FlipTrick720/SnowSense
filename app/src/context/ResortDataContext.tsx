import React, { createContext, useState, useEffect, useContext, ReactNode } from 'react';

//  Interfaces
export interface ResortDetail { 
    resortId: number; 
    resortName: string; 
    latitude: number;
    longitude: number;
    dangerLevel: string; 
    recommendation: string; 
    safetyStatus: string; 
    avalancheRegionName: string; 
    tendencyType: string; 
}

export interface Lift { id: number; name: string; isOpen: boolean; lengthInMeters: number; skiResort: { id: number; name: string }; }
export interface Slope { id: number; name: string; isOpen: boolean; difficultyLevel: string; skiResort: { id: number; name: string }; }
export interface Weather { temperature: number; skiResort: { id: number }; }

export interface NearestResort {
    resort: ResortDetail | null;
    distance: number | null;
}

interface ResortDataContextType {
    avalancheData: ResortDetail[];
    lifts: Lift[];
    slopes: Slope[];
    weather: Weather[];
    loading: boolean;
    error: string | null;
    refreshData: () => Promise<void>;
    nearestResort: NearestResort;
}

const ResortDataContext = createContext<ResortDataContextType | undefined>(undefined);

// Distance calc
const calculateDistance = (lat1: number, lon1: number, lat2: number, lon2: number): number => {
    const toRad = (value: number) => (value * Math.PI) / 180;
    const R = 6371; 
    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c; 
};

export const ResortDataProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [avalancheData, setAvalancheData] = useState<ResortDetail[]>([]);
    const [lifts, setLifts] = useState<Lift[]>([]);
    const [slopes, setSlopes] = useState<Slope[]>([]);
    const [weather, setWeather] = useState<Weather[]>([]);
    
    const [nearestResort, setNearestResort] = useState<NearestResort>({ resort: null, distance: null });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const getUserLocationAndFindNearest = (resorts: ResortDetail[], allLifts: Lift[]) => {
        if (!navigator.geolocation) {
            console.warn("Geolocation is not supported.");
            return;
        }

        navigator.geolocation.getCurrentPosition(
            (position) => {
                const userLat = position.coords.latitude;
                const userLon = position.coords.longitude;
                
                // calculation distance for ALL resorts 
                const resortsWithDist = resorts.map(resort => {
                    let dist = Infinity;
                    if (resort.latitude && resort.longitude) {
                        dist = calculateDistance(userLat, userLon, resort.latitude, resort.longitude);
                    }
                    return { resort, distance: dist };
                });

                resortsWithDist.sort((a, b) => a.distance - b.distance);

                const validResort = resortsWithDist.find(item => {
                    const resortLifts = allLifts.filter(l => l.skiResort.id === item.resort.resortId);
                    const openCount = resortLifts.filter(l => l.isOpen).length;
                    
                    return openCount > 0;
                });

                if (validResort) {
                    setNearestResort({ resort: validResort.resort, distance: validResort.distance });
                } else if (resortsWithDist.length > 0) {
                    // if ALL resorts are closed, just show the closest one anyway
                    setNearestResort({ resort: resortsWithDist[0].resort, distance: resortsWithDist[0].distance });
                }
            },
            (err) => console.error("Error getting location:", err)
        );
    };

    const fetchData = async () => {
        setLoading(true);
        setError(null);
        try {
            const [avalancheRes, liftsRes, slopesRes, weatherRes] = await Promise.all([
                fetch('http://localhost:8080/api/resorts/with-avalanche'),
                fetch('http://localhost:8080/api/skiresort/lifts'),
                fetch('http://localhost:8080/api/skiresort/slopes'),
                fetch('http://localhost:8080/api/weather')
            ]);

            if (!avalancheRes.ok || !liftsRes.ok || !slopesRes.ok || !weatherRes.ok) {
                throw new Error("Failed to fetch one or more APIs");
            }

            const resortData = await avalancheRes.json();
            const liftData = await liftsRes.json();

            setAvalancheData(resortData);
            setLifts(liftData);
            setSlopes(await slopesRes.json());
            setWeather(await weatherRes.json());

            getUserLocationAndFindNearest(resortData, liftData);

        } catch (err: any) {
            console.error("Context Fetch Error:", err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <ResortDataContext.Provider value={{ 
            avalancheData, lifts, slopes, weather, loading, error, refreshData: fetchData, nearestResort 
        }}>
            {children}
        </ResortDataContext.Provider>
    );
};

export const useResortData = () => {
    const context = useContext(ResortDataContext);
    if (!context) throw new Error("useResortData must be used within a ResortDataProvider");
    return context;
};