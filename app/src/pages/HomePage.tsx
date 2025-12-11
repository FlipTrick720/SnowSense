import React, { useEffect, useState } from 'react';
import { IonCol, IonContent, IonGrid, IonLabel, IonPage, IonRow, IonSpinner } from '@ionic/react';
import LiftCard from "../components/LiftCard";
import RecCard from "../components/RecCard";
import ResortList from "../components/ResortList";

import { useResortData } from '../context/ResortDataContext';
export interface MergedResortData { 
    id: number;
    resortName: string; 
    temperature: number | null; 
    recommendation: string | null;
    openLifts: string; 
    openSlopes: string; 
}



const HomePage: React.FC = () => {
    // all data from context
    const { avalancheData, lifts, slopes, weather, loading, error,nearestResort } = useResortData();
    
    const [resorts, setResorts] = useState<MergedResortData[]>([]);
    const [liftStats, setLiftStats] = useState({ open: 0, total: 0 });
    const [recResort, setRecResort] = useState({name: 'N/A', lifts: 'N/A'});

    const getRecCardProps = () => {
        if (nearestResort.resort) {
            // find open lifts for this specific nearest resort
            const resortLifts = lifts.filter(l => l.skiResort.id === nearestResort.resort?.resortId);
            const openCount = resortLifts.filter(l => l.isOpen).length;
            const totalCount = resortLifts.length;
            const distance = nearestResort.distance ? `${nearestResort.distance.toFixed(1)} km away` : '';

            return {
                resortName: nearestResort.resort.resortName,
                liftStatus: `${distance} • ${openCount}/${totalCount} Lifts` 
            };
        }
        
        // fallback if location fails
        return {
            resortName: "Mayrhofen",
            liftStatus: "Recommended"
        };
    };

    const recProps = getRecCardProps();

    useEffect(() => {
        if (loading || error) return;

        // --- Process Data (Logic moved from fetch to here) ---
        let totalOpenLifts = 0;
        let totalAllLifts = 0;

        const liftStatsMap = new Map<string, { open: number, total: number }>();
        lifts.forEach(lift => {
            const resortName = lift.skiResort.name;
            const stats = liftStatsMap.get(resortName) || { open: 0, total: 0 };
            stats.total++;
            if (lift.isOpen) stats.open++;
            liftStatsMap.set(resortName, stats);
        });
        
        liftStatsMap.forEach(stats => {
            totalOpenLifts += stats.open;
            totalAllLifts += stats.total;
        });
        setLiftStats({ open: totalOpenLifts, total: totalAllLifts });

        const slopeStatsMap = new Map<string, { open: number, total: number }>();
        slopes.forEach(slope => {
            const resortName = slope.skiResort.name;
            const stats = slopeStatsMap.get(resortName) || { open: 0, total: 0 };
            stats.total++;
            if (slope.isOpen) stats.open++;
            slopeStatsMap.set(resortName, stats);
        });

        const weatherMap = new Map<string, number>();
        weather.forEach(w => {
            weatherMap.set(w.skiResort.id.toString(), w.temperature); // map by ID or Name based
        });
        
        // Merge data
        const mergedData: MergedResortData[] = avalancheData.map(a => {
            const liftStats = liftStatsMap.get(a.resortName);
            const slopeStats = slopeStatsMap.get(a.resortName); 
            
            // Note: Ensures weather matching logic aligns with api
            const temp = weather.find(w => w.skiResort.id === a.resortId)?.temperature || null;

            return {
                id: a.resortId,
                resortName: a.resortName,
                recommendation: a.recommendation,
                temperature: temp,
                openLifts: liftStats ? `${liftStats.open}/${liftStats.total}` : 'N/A',
                openSlopes: slopeStats ? `${slopeStats.open}/${slopeStats.total} Slopes` : 'Slopes N/A', 
            };
        });
        
        setResorts(mergedData);
        
        // for RecCard logics
        const recData = mergedData.find(r => r.resortName === 'Mayrhofen') || mergedData[0];
        if (recData) {
            setRecResort({
                name: recData.resortName,
                lifts: recData.openLifts.includes('/') 
                    ? `${recData.openLifts.split('/')[0].trim()} Lifts open`
                    : 'Lifts N/A'
            });
        }

    }, [avalancheData, lifts, slopes, weather, loading, error]);

    if (loading) return <IonPage><IonContent className="ion-text-center ion-padding"><IonSpinner name="crescent" /></IonContent></IonPage>;
    if (error) return <IonPage><IonContent className="ion-padding"><IonLabel color="danger">Error: {error}</IonLabel></IonContent></IonPage>;
    
    return (
        <IonPage>
            <IonContent fullscreen>
                <IonGrid className="ion-padding-vertical">
                    <IonRow className="ion-justify-content-center">
                        <IonCol className="ion-padding-horizontal-sm"><LiftCard openCount={liftStats.open} totalCount={liftStats.total}/></IonCol>
                        <IonCol className="ion-padding-horizontal-sm">
                     <RecCard 
                         resortName={recProps.resortName}
                         liftStatus={recProps.liftStatus}
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