import React from "react";
import { IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle } from "@ionic/react";

interface RecCardProps {
    resortName: string;
    liftStatus: string;
}

const RecCard: React.FC<RecCardProps> = ({ resortName, liftStatus }) => {
    return (
        <IonCard color="tertiary" style={{ minHeight: "120px" }}>
            <IonCardHeader>
                <IonCardSubtitle style={{ fontWeight: '400', lineHeight: '14px' }}>
                    Nearest Resort
                </IonCardSubtitle>
                <IonCardTitle style={{ fontWeight: '600', fontSize: '20px' }}>
                    {resortName}
                </IonCardTitle>
            </IonCardHeader>

            <IonCardContent style={{ lineHeight: "14px", fontSize: '13px' }}>
                {liftStatus}
            </IonCardContent>
        </IonCard>
    )
}

export default RecCard;