import React from "react";
import { IonList, IonListHeader, IonLabel, IonItem, IonGrid, IonRow, IonCol, IonNote } from "@ionic/react";

interface MergedResortData { 
    id: number;
    resortName: string; 
    temperature: number | null; 
    recommendation: string | null;
    openLifts: string;
    openSlopes: string; 
}

interface ResortListProps {
    resorts: MergedResortData[];
}

const ResortList: React.FC<ResortListProps> = ({ resorts }) => {
    const infoColor = 'var(--ion-color-step-500, #737373)'; 
    const statsColor = 'var(--ion-color-step-600, #5b5764)'; 

    return(
        <IonList lines="full" style={{ width: '100%' }} className="ion-padding-horizontal">
            <IonListHeader>
                <IonLabel style={{ fontWeight: '600', fontSize: '1.375rem' }}>Resorts</IonLabel>
            </IonListHeader>
            
            {resorts.map((item, index) => (
                <IonItem
                    key={index} 
                    detail={false} 
                    lines="full" 
                    className="ion-no-padding-start ion-no-padding-end"
                    routerLink={`/resort/${item.id}`}
                >
                    <IonGrid className="ion-no-padding">
                        <IonRow className="ion-align-items-center ion-justify-content-between">
                            <IonCol size="auto" className="ion-no-padding ion-padding-vertical">
                                <IonLabel>
                                    <h2 className="ion-no-margin" style={{ fontWeight: '600', color: 'var(--ion-color-dark)' }}>{item.resortName}</h2>
                                    <p className="ion-no-margin" style={{ color: infoColor, fontSize: '14px' }}>{item.openSlopes}</p>
                                </IonLabel>
                            </IonCol>
                            <IonCol size="auto" className="ion-no-padding ion-text-end">
                                <IonLabel>
                                    <h3 className="ion-no-margin" style={{ fontWeight: '600', color: 'var(--ion-color-dark)' }}>
                                        {item.temperature !== null ? `${item.temperature.toFixed(0)}°C` : 'N/A'}
                                    </h3>
                                    <p className="ion-no-margin" style={{ color: statsColor, fontSize: '14px', lineHeight: '18px' }}>
                                        {`Lifts `+ item.openLifts}
                                    </p>
                                </IonLabel>
                            </IonCol>
                        </IonRow>
                    </IonGrid>
                </IonItem>
            ))}

            {resorts.length === 0 && (
                <IonItem><IonLabel><IonNote>No resort data loaded.</IonNote></IonLabel></IonItem>
            )}
        </IonList>
    );
};

export default ResortList;