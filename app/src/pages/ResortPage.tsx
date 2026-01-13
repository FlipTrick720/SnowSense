import React, { useEffect, useState } from 'react';
import { 
    IonPage, IonHeader, IonToolbar, IonButtons, IonBackButton, IonTitle, IonContent, 
    IonGrid, IonRow, IonCol, IonIcon, IonText, IonSpinner, IonLabel
} from '@ionic/react';
import { RouteComponentProps } from 'react-router';
import { checkmarkCircle, closeCircle, thermometer, alertCircle } from 'ionicons/icons';
import { useResortData } from '../context/ResortDataContext';

interface ResortPageProps extends RouteComponentProps<{ id: string }> {}

const ResortPage: React.FC<ResortPageProps> = ({ match }) => {
    const resortId = parseInt(match.params.id, 10);
    
    const { avalancheData, lifts, slopes, weather, loading } = useResortData();

    const [pageDetail, setPageDetail] = useState<any>(null);
    const [pageLifts, setPageLifts] = useState<any[]>([]);
    const [pageSlopes, setPageSlopes] = useState<any[]>([]);
    const [pageWeather, setPageWeather] = useState<any>(null);

    const DARK_TEXT_COLOR = 'var(--ion-color-dark, #1c1b1f)';
    const MEDIUM_TEXT_COLOR = 'var(--ion-color-medium, #929292)';

    useEffect(() => {
        if (!loading && resortId) {
            // filter the global lists for this specific ID
            setPageDetail(avalancheData.find(r => r.resortId === resortId));
            setPageLifts(lifts.filter(l => l.skiResort.id === resortId));
            setPageSlopes(slopes.filter(s => s.skiResort.id === resortId));
            setPageWeather(weather.find(w => w.skiResort.id === resortId));
        }
    }, [loading, resortId, avalancheData, lifts, slopes, weather]);


    if (loading) return <IonPage><IonContent className="ion-text-center ion-padding"><IonSpinner /></IonContent></IonPage>;

    if (!pageDetail) {
        return (
            <IonPage>
                <IonHeader className="ion-no-border">
                    <IonToolbar>
                        <IonButtons slot="start"><IonBackButton defaultHref="/app/home" color="dark" /></IonButtons>
                        <IonTitle>Resort</IonTitle>
                    </IonToolbar>
                </IonHeader>
                <IonContent className="ion-padding"><div style={{textAlign: 'center', marginTop: '20px'}}><IonLabel>Resort not found (ID: {resortId})</IonLabel></div></IonContent>
            </IonPage>
        );
    }

    const openLiftsCount = pageLifts.filter(l => l.isOpen).length;
    const openSlopesCount = pageSlopes.filter(s => s.isOpen).length;
    const totalSlopes = pageSlopes.length;

    const getDangerColor = (level: string) => {
        if (!level) return 'medium';
        switch (level.toLowerCase()) {
            case 'low': return 'success';
            case 'moderate': return 'warning';
            case 'high': return 'danger';
            default: return 'medium';
        }
    };

    return (
        <IonPage>
            <IonHeader className="ion-no-border">
                <IonToolbar>
                    <IonButtons slot="start"><IonBackButton defaultHref="/app/home" color="dark" /></IonButtons>
                    <IonTitle style={{fontWeight: 'bold', fontSize: '18px', color: DARK_TEXT_COLOR}}>{pageDetail.resortName}</IonTitle>
                    <IonButtons slot="end"><IonIcon name="notifications-outline" style={{fontSize: '24px', marginRight: '16px', color: DARK_TEXT_COLOR}} /></IonButtons>
                </IonToolbar>
            </IonHeader>

            <IonContent fullscreen className="ion-padding">
                
                <div className="ion-text-center ion-padding-bottom">
                    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '10px' }}>
                        <h1 style={{ fontSize: '48px', fontWeight: 'bold', margin: '0', color: DARK_TEXT_COLOR }}>
                            {pageWeather ? `${pageWeather.temperature.toFixed(0)}°C` : '--'}
                        </h1>
                        <IonIcon icon={thermometer} style={{ fontSize: '40px', color: DARK_TEXT_COLOR }} />
                    </div>
                    
                    <div style={{ color: MEDIUM_TEXT_COLOR, fontSize: '14px', marginTop: '10px' }}>
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '5px' }}>
                            <IonIcon icon={alertCircle} color={getDangerColor(pageDetail.dangerLevel)} />
                            <IonText color={getDangerColor(pageDetail.dangerLevel)} style={{fontWeight: '500'}}>
                                {pageDetail.dangerLevel?.toUpperCase()} Danger
                            </IonText>
                        </div>
                        <p style={{ margin: '5px 0 0', color: MEDIUM_TEXT_COLOR }}>
                            Tendency: {pageDetail.tendencyType?.toUpperCase() || 'N/A'}
                        </p>
                        <p style={{ margin: '0', fontSize: '12px' }}>
                            Region: {pageDetail.avalancheRegionName}
                        </p>
                    </div>
                </div>

                {/* Summary card */}
                <div style={{ backgroundColor: '#7c7c7ca9', borderRadius: '16px', padding: '16px', marginBottom: '24px' }}>
                    <h3 style={{ textAlign: 'center', fontWeight: 'bold', marginTop: '0'}}>Lifts and slopes</h3>
                    <IonGrid>
                        <IonRow>
                            <IonCol className="ion-text-center">
                                <div style={{color: MEDIUM_TEXT_COLOR, fontSize: '14px'}}>Open lifts</div>
                                <div style={{fontWeight: '500', color: DARK_TEXT_COLOR}}>{openLiftsCount} of {pageLifts.length}</div>
                            </IonCol>
                            <IonCol className="ion-text-center">
                                <div style={{color: MEDIUM_TEXT_COLOR, fontSize: '14px'}}>Open slopes</div>
                                <div style={{fontWeight: '500', color: DARK_TEXT_COLOR}}>{openSlopesCount} of {totalSlopes}</div>
                            </IonCol>
                        </IonRow>
                    </IonGrid>
                </div>

                {/* Lifts list */}
                <h3 style={{ fontWeight: 'bold', fontSize: '18px', marginBottom: '10px', color: DARK_TEXT_COLOR }}>Lifts</h3>
                <div style={{ marginBottom: '24px' }}>
                    {pageLifts.map((lift: any) => (
                        <div key={lift.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <IonIcon icon={lift.isOpen ? checkmarkCircle : closeCircle} color={lift.isOpen ? "success" : "danger"} />
                                <IonText style={{ fontWeight: '600', fontSize: '16px', color: DARK_TEXT_COLOR }}>{lift.name}</IonText>
                            </div>
                            <IonText style={{ fontWeight: '600', fontSize: '16px', color: DARK_TEXT_COLOR }}>{lift.lengthInMeters} m</IonText>
                        </div>
                    ))}
                </div>

                {/* Slopes list */}
                <h3 style={{ fontWeight: 'bold', fontSize: '18px', marginBottom: '10px', color: DARK_TEXT_COLOR }}>Slopes</h3>
                <div>
                    {pageSlopes.map((slope: any) => (
                        <div key={slope.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <IonIcon icon={slope.isOpen ? checkmarkCircle : closeCircle} color={slope.isOpen ? "success" : "danger"} />
                                <IonText style={{ fontWeight: '600', fontSize: '16px', color: DARK_TEXT_COLOR }}>{slope.name}</IonText>
                            </div>
                            <IonText style={{ fontWeight: '600', fontSize: '16px', color: DARK_TEXT_COLOR }}>{slope.difficultyLevel.toUpperCase()}</IonText>
                        </div>
                    ))}
                </div>

            </IonContent>
        </IonPage>
    );
};

export default ResortPage;