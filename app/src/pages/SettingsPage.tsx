import React from 'react';
import {
    IonPage,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonButtons,
    IonBackButton,
    IonToggle,
    IonList, IonItem
} from '@ionic/react';

const SettingsPage: React.FC = () => {
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton defaultHref="/app/home/"/>
                    </IonButtons>
                    <IonTitle>Settings</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent fullscreen >

                <IonList className="ion-padding" >
                    <IonItem >
                        <IonToggle >Receive Push Notifications</IonToggle>
                    </IonItem>

                </IonList>
            </IonContent>
        </IonPage>
    );
};

export default SettingsPage;