import React, { useEffect, useState } from 'react';
import {
    IonPage, IonHeader, IonToolbar, IonTitle, IonContent, IonCard, IonCardHeader, IonCardTitle,
    IonCardContent, IonSpinner, IonText, IonIcon, IonChip, IonLabel, IonButton, IonGrid, IonRow, IonCol, IonNote
} from '@ionic/react';
import { location, star, thermometer, alertCircle, arrowForward } from 'ionicons/icons';
import { useHistory } from 'react-router';
import { useResortData } from '../context/ResortDataContext';

interface RecommendedResort {
    resort: {
        id: number;
        name: string;
        latitude: number;
        longitude: number;
        elevation: number;
    };
    distanceKm: number;
    penaltyScore: number;
}

interface EnrichedRecommendation extends RecommendedResort {
    dangerLevel?: string;
    safetyStatus?: string;
    recommendation?: string;
    temperature?: number;
}

const RecommendationsPage: React.FC = () => {
    const history = useHistory();
    const { avalancheData, weather, userLocation } = useResortData();
    const [recommendations, setRecommendations] = useState<EnrichedRecommendation[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const DARK_TEXT_COLOR = 'var(--ion-color-dark, #1c1b1f)';
    const MEDIUM_TEXT_COLOR = 'var(--ion-color-medium, #929292)';

    useEffect(() => {
        // Auto-fetch when component mounts and user location is available
        if (userLocation) {
            fetchRecommendations();
        }
    }, [userLocation]);

    const fetchRecommendations = async () => {
        setLoading(true);
        setError(null);

        try {
            if (!userLocation) {
                setError('User location not available. Please enable location services.');
                setLoading(false);
                return;
            }

            const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:7860';
            const lat = userLocation.latitude;
            const lon = userLocation.longitude;
            const url = `${API_URL}/api/recommendation/skiresort?latitude=${lat}&longitude=${lon}`;
            
            console.log('Fetching recommendations from:', url);
            
            const response = await fetch(url, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) {
                throw new Error(`API Error: ${response.status} ${response.statusText}`);
            }

            const data = await response.json();
            console.log('Recommendations received:', data);
            
            // Enrich recommendations with avalanche and weather data
            const enriched: EnrichedRecommendation[] = (data.recommendations || []).map((rec: RecommendedResort) => {
                const avalanche = avalancheData.find(a => a.resortId === rec.resort.id);
                const weatherData = weather.find(w => w.skiResort.id === rec.resort.id);

                return {
                    ...rec,
                    dangerLevel: avalanche?.dangerLevel,
                    safetyStatus: avalanche?.safetyStatus,
                    recommendation: avalanche?.recommendation,
                    temperature: weatherData?.temperature
                };
            });

            setRecommendations(enriched);
        } catch (err: any) {
            console.error('Failed to fetch recommendations:', err);
            setError(`Error: ${err.message}. Please try again or check your connection.`);
        } finally {
            setLoading(false);
        }
    };

    const getDangerColor = (level: string | null | undefined) => {
        if (!level) return 'medium';
        switch (level.toLowerCase()) {
            case 'low': return 'success';
            case 'moderate': return 'warning';
            case 'considerable': return 'danger';
            case 'high': return 'danger';
            case 'very_high': return 'danger';
            default: return 'medium';
        }
    };

    const getSafetyIcon = (status: string | undefined) => {
        switch (status?.toUpperCase()) {
            case 'SAFE':
                return { icon: star, color: 'success' };
            case 'CAUTION':
                return { icon: alertCircle, color: 'warning' };
            case 'WARNING':
                return { icon: alertCircle, color: 'danger' };
            default:
                return { icon: alertCircle, color: 'medium' };
        }
    };

    if (loading) {
        return (
            <IonPage>
                <IonHeader className="ion-no-border">
                    <IonToolbar>
                        <IonTitle style={{ fontWeight: 'bold', fontSize: '20px', color: DARK_TEXT_COLOR }}>
                            Recommended Resorts
                        </IonTitle>
                    </IonToolbar>
                </IonHeader>
                <IonContent className="ion-text-center ion-padding">
                    <IonSpinner />
                </IonContent>
            </IonPage>
        );
    }

    return (
        <IonPage>
            <IonHeader className="ion-no-border">
                <IonToolbar>
                    <IonTitle style={{ fontWeight: 'bold', fontSize: '20px', color: DARK_TEXT_COLOR }}>
                        Recommended Resorts
                    </IonTitle>
                </IonToolbar>
            </IonHeader>

            <IonContent fullscreen className="ion-padding">
                {error && (
                    <IonCard style={{ backgroundColor: '#f8d7da', borderRadius: '12px', marginBottom: '16px' }}>
                        <IonCardContent style={{ color: '#721c24', fontSize: '14px' }}>
                            {error}
                        </IonCardContent>
                    </IonCard>
                )}

                {!error && recommendations.length === 0 && (
                    <IonCard style={{ textAlign: 'center', padding: '32px' }}>
                        <IonCardContent>
                            <IonText color="medium">
                                <p>No recommendations available at the moment.</p>
                                <p style={{ fontSize: '12px' }}>All nearby resorts may have avalanche warnings.</p>
                            </IonText>
                            <IonButton color="primary" onClick={fetchRecommendations}>
                                Try Again
                            </IonButton>
                        </IonCardContent>
                    </IonCard>
                )}

                {recommendations.map((rec, index) => {
                    const safetyIcon = getSafetyIcon(rec.safetyStatus);
                    return (
                        <IonCard
                            key={rec.resort.id}
                            style={{
                                marginBottom: '16px',
                                borderRadius: '12px',
                                border: `2px solid ${getDangerColor(rec.dangerLevel) === 'success' ? '#2dd36f' : getDangerColor(rec.dangerLevel) === 'warning' ? '#ffc409' : '#eb445a'}`,
                                cursor: 'pointer'
                            }}
                            onClick={() => history.push(`/resort/${rec.resort.id}`)}
                        >
                            <IonCardHeader>
                                <IonGrid>
                                    <IonRow>
                                        <IonCol>
                                            <IonCardTitle style={{ fontWeight: 'bold', fontSize: '18px', color: DARK_TEXT_COLOR }}>
                                                #{index + 1} {rec.resort.name}
                                            </IonCardTitle>
                                        </IonCol>
                                        <IonCol size="auto">
                                            <IonChip
                                                color={safetyIcon.color}
                                                style={{ fontWeight: '600', fontSize: '12px' }}
                                            >
                                                <IonIcon icon={safetyIcon.icon} />
                                                <IonLabel>{rec.safetyStatus || 'UNKNOWN'}</IonLabel>
                                            </IonChip>
                                        </IonCol>
                                    </IonRow>
                                </IonGrid>
                            </IonCardHeader>

                            <IonCardContent>
                                {/* Danger Level */}
                                {rec.dangerLevel && (
                                    <div style={{ marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <IonIcon
                                            icon={alertCircle}
                                            color={getDangerColor(rec.dangerLevel)}
                                            style={{ fontSize: '18px' }}
                                        />
                                        <IonText color={getDangerColor(rec.dangerLevel)} style={{ fontWeight: '600' }}>
                                            {rec.dangerLevel.toUpperCase()} Danger
                                        </IonText>
                                    </div>
                                )}

                                {/* Temperature */}
                                {rec.temperature !== undefined && (
                                    <div style={{ marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <IonIcon icon={thermometer} style={{ color: DARK_TEXT_COLOR }} />
                                        <IonText style={{ color: DARK_TEXT_COLOR }}>
                                            {rec.temperature.toFixed(1)}°C
                                        </IonText>
                                    </div>
                                )}

                                {/* Distance */}
                                <div style={{ marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                                    <IonIcon icon={location} style={{ color: DARK_TEXT_COLOR }} />
                                    <IonText style={{ color: DARK_TEXT_COLOR }}>
                                        {rec.distanceKm.toFixed(1)} km away
                                    </IonText>
                                </div>

                                {/* Penalty Score (hidden but available for debugging) */}
                                <IonNote style={{ fontSize: '11px', color: MEDIUM_TEXT_COLOR }}>
                                    Score: {rec.penaltyScore.toFixed(1)}
                                </IonNote>

                                {/* Recommendation */}
                                {rec.recommendation && (
                                    <div style={{ 
                                        backgroundColor: '#e8f4f8', 
                                        borderRadius: '8px', 
                                        padding: '10px', 
                                        marginTop: '12px',
                                        marginBottom: '12px',
                                        fontSize: '12px',
                                        color: '#1b5e79',
                                        lineHeight: '1.5'
                                    }}>
                                        {rec.recommendation}
                                    </div>
                                )}

                                {/* View Details Button */}
                                <IonButton
                                    expand="block"
                                    color="primary"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        history.push(`/resort/${rec.resort.id}`);
                                    }}
                                    style={{ marginTop: '10px' }}
                                >
                                    View Details
                                    <IonIcon slot="end" icon={arrowForward} />
                                </IonButton>
                            </IonCardContent>
                        </IonCard>
                    );
                })}

                {!loading && recommendations.length > 0 && (
                    <IonButton
                        expand="block"
                        fill="outline"
                        onClick={fetchRecommendations}
                        style={{ marginTop: '16px' }}
                    >
                        Refresh Recommendations
                    </IonButton>
                )}
            </IonContent>
        </IonPage>
    );
};

export default RecommendationsPage;
