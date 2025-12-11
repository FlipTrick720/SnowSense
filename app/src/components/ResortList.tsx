// src/components/ResortList.tsx

import React from "react";
import { IonList, IonListHeader, IonLabel, IonItem, IonGrid, IonRow, IonCol, IonNote } from "@ionic/react";

// Define the shape of the data expected via props
interface MergedResortData { 
    resortName: string; 
    temperature: number | null; 
    recommendation: string | null;
    openLifts: string; // e.g., "50/120" or "N/A"
}

// Props for the list component
interface ResortListProps {
    resorts: MergedResortData[];
}

const ResortList: React.FC<ResortListProps> = ({ resorts }) => {
    // Colors based on design image
    const infoColor = 'var(--ion-color-step-500, #737373)'; 
    const statsColor = 'var(--ion-color-step-600, #5b5764)'; 

    return(
        <IonList lines="full"  style={{ width: '100%' }} className="ion-padding-horizontal">
            
            <IonListHeader>
                <IonLabel style={{ fontWeight: '600', fontSize: '1.375rem' }}>Resorts</IonLabel>
            </IonListHeader>
            
            {resorts.map((item, index) => (
                <IonItem
                    key={index} 
                    detail={false} 
                    lines="full" 
                    className="ion-no-padding-start ion-no-padding-end"
                >
                    <IonGrid className="ion-no-padding">
                        <IonRow className="ion-align-items-center ion-justify-content-between">

                            {/* Left Column: Resort Name and "Location info" */}
                            <IonCol size="auto" className="ion-no-padding ion-padding-vertical">
                                <IonLabel>
                                    <h2 className="ion-no-margin" style={{ fontWeight: '600', color: 'var(--ion-color-dark)' }}>
                                        {item.resortName}
                                    </h2>
                                    {/* Sub-text: Hardcoded to match the "Location info" text in the image */}
                                    <p className="ion-no-margin" style={{ color: infoColor, fontSize: '14px' }}>
                                        Location info
                                    </p>
                                </IonLabel>
                            </IonCol>

                            {/* Right Column: Temperature and Open Lifts */}
                            <IonCol size="auto" className="ion-no-padding ion-text-end">
                                <IonLabel>
                                    {/* Weather Temperature */}
                                    <h3 className="ion-no-margin" style={{ fontWeight: '600', color: 'var(--ion-color-dark)' }}>
                                        {item.temperature !== null ? `${item.temperature.toFixed(0)}°C` : 'N/A'}
                                    </h3>
                                    
                                    {/* Open Lifts Status */}
                                    <p className="ion-no-margin" style={{ color: statsColor, fontSize: '14px', lineHeight: '18px' }}>
                                        {item.openLifts}
                                    </p>
                                </IonLabel>
                            </IonCol>

                        </IonRow>
                    </IonGrid>
                </IonItem>
            ))}

            {/* Fallback for empty data */}
            {resorts.length === 0 && (
                <IonItem>
                    <IonLabel><IonNote>No resort data loaded.</IonNote></IonLabel>
                </IonItem>
            )}
        </IonList>
    );
};

export default ResortList;