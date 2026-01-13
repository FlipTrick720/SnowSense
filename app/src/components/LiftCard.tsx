import React from "react";
import { IonCard, IonCardContent, IonCardHeader, IonCardTitle } from "@ionic/react";

// 1. Define the props interface to accept the dynamic numbers
interface LiftCardProps {
    openCount: number;
    totalCount: number;
}

// 2. Pass the props into the component
const LiftCard: React.FC<LiftCardProps> = ({ openCount, totalCount }) => {
    return (
        <IonCard style={{ minHeight: "120px" }}>
            <IonCardHeader>
                {/* 3. Render the dynamic values instead of hardcoded 50/120 */}
                <IonCardTitle style={{ fontWeight: '500', fontSize: '32px' }}>
                    {openCount}/{totalCount}
                </IonCardTitle>
            </IonCardHeader>

            <IonCardContent>Lifts Open</IonCardContent>

            {/* <IonButton fill="clear">View all</IonButton> */}
        </IonCard>
    );
}

export default LiftCard;