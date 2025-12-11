import React from "react";
import { IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle} from "@ionic/react";


const RecCard: React.FC =()=>{
    return (
        <IonCard color="tertiary" style={{minHeight: "120px"}}>
            <IonCardHeader>
                <IonCardSubtitle style={{ fontWeight: '400', lineHeight:'8px'}}>Recommendation</IonCardSubtitle>
                <IonCardTitle style={{ fontWeight: '600', fontSize: '20px'  }}>Mayrhofen</IonCardTitle>
            </IonCardHeader>

            <IonCardContent style={{lineHeight:"10px"}}>20 Lifts Open</IonCardContent>

        </IonCard>
    )
}

export default RecCard;