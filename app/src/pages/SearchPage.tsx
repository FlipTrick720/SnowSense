import React, { useState, useEffect } from 'react';
import {
    IonBackButton, IonButtons, IonContent, IonHeader, IonItem, IonList, IonPage,
    IonSearchbar, IonTitle, IonToolbar, IonLabel, IonNote
} from '@ionic/react';

// FontAwesome icons
import { FaMountain, FaTram, FaSkiing } from "react-icons/fa";

import { useResortData } from '../context/ResortDataContext';

interface SearchResult {
    uniqueId: string;
    label: string;
    type: 'Resort' | 'Lift' | 'Slope';
    resortId: number;
}

const SearchPage: React.FC = () => {
    const { avalancheData, lifts, slopes } = useResortData();
    const [results, setResults] = useState<SearchResult[]>([]);
    const [query, setQuery] = useState('');

    useEffect(() => {
        if (query === '') {
            const initialResorts = avalancheData.map(r => ({
                uniqueId: `resort-${r.resortId}`,
                label: r.resortName,
                type: 'Resort' as const,
                resortId: r.resortId
            }));
            setResults(initialResorts);
        }
    }, [avalancheData, query]);

    const handleInput = (event: Event) => {
        const target = event.target as HTMLIonSearchbarElement;
        const value = target.value?.toLowerCase() || '';
        setQuery(value);

        if (value.trim() === '') {
            setResults(avalancheData.map(r => ({
                uniqueId: `resort-${r.resortId}`,
                label: r.resortName,
                type: 'Resort' as const,
                resortId: r.resortId
            })));
            return;
        }

        const filteredResorts: SearchResult[] = avalancheData
            .filter(r => r.resortName.toLowerCase().includes(value))
            .map(r => ({
                uniqueId: `resort-${r.resortId}`,
                label: r.resortName,
                type: 'Resort',
                resortId: r.resortId
            }));

        const filteredLifts: SearchResult[] = lifts
            .filter(l => l.name.toLowerCase().includes(value))
            .map(l => ({
                uniqueId: `lift-${l.id}`,
                label: l.name,
                type: 'Lift',
                resortId: l.skiResort.id
            }));

        const filteredSlopes: SearchResult[] = slopes
            .filter(s => s.name.toLowerCase().includes(value))
            .map(s => ({
                uniqueId: `slope-${s.id}`,
                label: s.name,
                type: 'Slope',
                resortId: s.skiResort.id
            }));

        setResults([...filteredResorts, ...filteredLifts, ...filteredSlopes]);
    };

    const renderIcon = (type: string) => {
        const iconStyle = { fontSize: '20px', color: '#92949c', marginRight: '16px' };
        
        switch (type) {
            case 'Resort': return <FaMountain />;
            case 'Lift': return <FaTram  />;  
            case 'Slope': return <FaSkiing />; 
            default: return <FaMountain  />;
        }
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
                <IonSearchbar 
                    debounce={300} 
                    onIonInput={(event) => handleInput(event)}
                    placeholder="Search resorts, lifts, slopes..."
                ></IonSearchbar>

                <IonList>
                    {results.map((result) => (
                        <IonItem 
                            key={result.uniqueId} 
                            routerLink={`/resort/${result.resortId}`} 
                            detail={true}
                        >
                            {/* render the icon directly in the 'start' slot */}
                            <div slot="start" style={{ display: 'flex', alignItems: 'center' }}>
                                {renderIcon(result.type)}
                            </div>

                            <IonLabel>
                                <h2>{result.label}</h2>
                                <p style={{ fontSize: '12px', color: 'gray' }}>{result.type}</p>
                            </IonLabel>
                            
                            {result.type !== 'Resort' && (
                                <IonNote slot="end" style={{fontSize: '12px'}}>
                                    Go to Resort
                                </IonNote>
                            )}
                        </IonItem>
                    ))}
                    
                    {results.length === 0 && (
                        <IonItem lines="none">
                            <IonLabel className="ion-text-center" color="medium">
                                No results found
                            </IonLabel>
                        </IonItem>
                    )}
                </IonList>
            </IonContent>
        </IonPage>
    );
}
export default SearchPage;