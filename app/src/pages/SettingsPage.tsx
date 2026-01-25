import React, { useEffect, useState } from 'react';
import {
    IonPage,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonButtons,
    IonBackButton,
    IonToggle,
    IonList, IonItem,
    IonToast
} from '@ionic/react';
import { 
    isPushSubscribed, 
    subscribeToPushNotifications, 
    unsubscribeFromPushNotifications 
} from '../pushNotificationService';

const SettingsPage: React.FC = () => {
    const [notificationsEnabled, setNotificationsEnabled] = useState(false);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');

    useEffect(() => {
        setNotificationsEnabled(isPushSubscribed());
    }, []);

    const handleToggleChange = async (e: CustomEvent) => {
        const isChecked = e.detail.checked;
        
        // Prevent infinite loop if state update triggers change
        if (isChecked === notificationsEnabled) return;

        try {
            if (isChecked) {
                await subscribeToPushNotifications();
                setToastMessage('Successfully subscribed to notifications');
            } else {
                await unsubscribeFromPushNotifications();
                setToastMessage('Successfully unsubscribed from notifications');
            }
            setNotificationsEnabled(isChecked);
        } catch (error) {
            console.error('Error toggling notifications:', error);
            // Revert state on error
            setNotificationsEnabled(!isChecked);
            setToastMessage('Failed to update notification settings');
        } finally {
            setShowToast(true);
        }
    };

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
                        <IonToggle 
                            checked={notificationsEnabled}
                            onIonChange={handleToggleChange}
                        >
                            Receive Push Notifications
                        </IonToggle>
                    </IonItem>

                </IonList>
                <IonToast
                    isOpen={showToast}
                    onDidDismiss={() => setShowToast(false)}
                    message={toastMessage}
                    duration={2000}
                />
            </IonContent>
        </IonPage>
    );
};

export default SettingsPage;