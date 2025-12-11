import React, { useState } from 'react';
import {
    IonBackButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonItem,
    IonList,
    IonPage,
    IonSearchbar,
    IonTitle,
    IonToolbar
} from '@ionic/react';

const SearchPage:React.FC = () => {
    const data = [
        'Mayrhofen',
        'Ischgl',
        'Kitzbühel',
        'Zillertal Arena',
        'Kaunertal Glacier',
    ];
    const [results, setResults] = useState([...data]);

    const handleInput = (event: Event) => {
        let query = '';
        const target = event.target as HTMLIonSearchbarElement;
        if (target) query = target.value!.toLowerCase();

        setResults(data.filter((d) => d.toLowerCase().indexOf(query) > -1));
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton defaultHref="/app/home/"/>
                    </IonButtons>
                    <IonTitle>Search</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent fullscreen>
                <IonSearchbar  onIonInput={(event) => handleInput(event)}></IonSearchbar>

                <IonList>
                    {results.map((result) => (
                        <IonItem>{result}</IonItem>
                    ))}
                </IonList>
            </IonContent>
        </IonPage>


    );
}
export default SearchPage;