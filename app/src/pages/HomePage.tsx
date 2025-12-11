import React, { useEffect, useState } from 'react';
import {IonCol, IonContent, IonGrid, IonLabel, IonPage, IonRow, IonSpinner} from '@ionic/react';
import LiftCard from "../components/LiftCard";
import RecCard from "../components/RecCard";
import ResortList from "../components/ResortList";

const API_AVALANCHE_URL = 'http://localhost:8080/api/resorts/with-avalanche';
const API_WEATHER_URL = 'http://localhost:8080/api/weather';
const API_LIFTS_URL = 'http://localhost:8080/api/skiresort/lifts'; 

// Interfaces
interface AvalancheData { resortName: string; dangerLevel: string | null; recommendation: string | null; }
interface WeatherData { skiResort: { name: string; id: number; }; temperature: number; }
interface LiftData { skiResort: { name: string; id: number; }; isOpen: boolean; } 

interface MergedResortData { 
    resortName: string; 
    temperature: number | null; 
    recommendation: string | null;
    openLifts: string; 
}

const HomePage: React.FC = () => {

    const [resorts, setResorts] = useState<MergedResortData[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    
    const [liftStats, setLiftStats] = useState({ open: 50, total: 120 }); // Set to design default
    const [recResort, setRecResort] = useState({name: 'Mayrhofen', lifts: '20 Lifts open'});

    
    useEffect(() => {
        const fetchData = async () => {
            let totalOpenLifts = 0;
            let totalAllLifts = 0;

            try {
                const [avalancheResponse, weatherResponse, liftsResponse] = await Promise.all([
                    fetch(API_AVALANCHE_URL),
                    fetch(API_WEATHER_URL),
                    fetch(API_LIFTS_URL), 
                ]);

                if (!avalancheResponse.ok || !weatherResponse.ok || !liftsResponse.ok) {
                    throw new Error('One or more APIs returned an error status.');
                }

                const avalancheData: AvalancheData[] = await avalancheResponse.json();
                const weatherData: WeatherData[] = await weatherResponse.json();
                const liftData: LiftData[] = await liftsResponse.json(); 

                const liftStatsMap = new Map<string, { open: number, total: number }>();
                
                liftData.forEach(lift => {
                    const resortName = lift.skiResort.name;
                    const stats = liftStatsMap.get(resortName) || { open: 0, total: 0 };
                    
                    stats.total++;
                    if (lift.isOpen) {
                        stats.open++;
                    }
                    liftStatsMap.set(resortName, stats);
                });
                
                liftStatsMap.forEach(stats => {
                    totalOpenLifts += stats.open;
                    totalAllLifts += stats.total;
                });
                setLiftStats({ open: totalOpenLifts, total: totalAllLifts });


                const weatherMap = new Map<string, number>();
                weatherData.forEach(w => {
                    weatherMap.set(w.skiResort.name, w.temperature); 
                });


                const mergedData: MergedResortData[] = avalancheData.map(a => {
                    const liftStats = liftStatsMap.get(a.resortName);
                    
                    const openLiftsString = liftStats 
                        ? `${liftStats.open}/${liftStats.total}` 
                        : 'N/A'; 
                    
                    return {
                        resortName: a.resortName,
                        recommendation: a.recommendation,
                        temperature: weatherMap.get(a.resortName) || null,
                        openLifts: openLiftsString, 
                    };
                });
                
                setResorts(mergedData);
                
                const recData = mergedData.find(r => r.resortName === 'Mayrhofen') || mergedData[0];
                if (recData) {
                    setRecResort({
                        name: recData.resortName,
                        lifts: recData.openLifts.includes('/') 
                            ? `${recData.openLifts.split('/')[0].trim()} Lifts open`
                            : 'Lifts N/A'
                    });
                }


            } catch (e: any) {
                console.error("Fetch error:", e);
                setError(`Could not load combined data. Check API endpoints.`);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return (
            <IonPage>
                <IonContent className="ion-text-center ion-padding">
                    <IonSpinner name="crescent" /><p>Loading combined forecast data...</p>
                </IonContent>
            </IonPage>
        );
    }

    if (error) {
        return (
            <IonPage>
                <IonContent className="ion-padding">
                    <IonLabel color="danger">Error: {error}</IonLabel>
                </IonContent>
            </IonPage>
        );
    }
    
    return (
        <IonPage>
            <IonContent fullscreen>
                <IonGrid className="ion-padding-vertical">
                    
                    <IonRow className="ion-justify-content-center">
                        <IonCol className="ion-padding-horizontal-sm">
                            <LiftCard 
                                openCount={liftStats.open} 
                                totalCount={liftStats.total}
                            />
                        </IonCol>
                        <IonCol className="ion-padding-horizontal-sm">
                            <RecCard 
                                resortName={recResort.name}
                                liftStatus={recResort.lifts}
                            />
                        </IonCol>
                    </IonRow>
                    
                    <IonRow className="ion-padding">
                        <ResortList resorts={resorts} /> 
                    </IonRow>

                </IonGrid>
            </IonContent>
        </IonPage>
    );
};

export default HomePage;