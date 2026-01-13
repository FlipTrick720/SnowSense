import React from 'react';
import { IonCard, IonText, IonIcon } from '@ionic/react';
import { arrowForward } from 'ionicons/icons';

const StatCard: React.FC = () => {

    // Hardcoded styles for the first card ("50/120 Lifts Open")
    const cardColor = 'medium'; // Matches Gray/Medium (#A09CAB)
    const darkTextColor = 'dark'; // Matches Black (#1C1B1F)

    // Base styles for IonCard (Frame)
    const cardStyle = {
        '--border-color': `var(--ion-color-medium)`,
        '--border-style': 'solid',
        '--border-width': '1px',
        '--border-radius': '12px',
        width: '45vw',
        height: 'auto',
        backgroundColor: 'white',
    };
    const viewAll=()=>{
        console.log('viewAll');

    }

    return (
        <IonCard onClick={viewAll}
            style={cardStyle}
            className="ion-no-margin ion-padding-vertical ion-padding-horizontal" > {/*12px 16px padding*/}

            {/* Resort Section (Flex column, gap: 2px, ion-padding-bottom simulates the 16px gap) */}
            <div className="ion-padding-bottom ion-align-items-start ion-display-flex ion-flex-wrap" style={{ gap: '2px' }}>

                {/* Value Text: 50/120 (Title/Large: 32px, Bold) */}
                <IonText color={darkTextColor}>
                    <h2 className="ion-no-margin" style={{ fontWeight: '600', fontSize: '28px',  }}>
                        50/120
                    </h2>
                </IonText>

                {/* Label Text: Lifts Open (Title/Small: 16px, Bold) */}
                <IonText color={darkTextColor}>
                    <p className="ion-no-margin" style={{ fontWeight: '600', fontSize: '14px',  }}>
                        Lifts Open
                    </p>
                </IonText>
            </div>

            {/* Info Section (Flex row, justify-content: space-between, gap: 2px) */}
            <div className="ion-align-items-center ion-display-flex ion-justify-content-between"
                 style={{ gap: '2px', width: '100%' }}>

                {/* Info Text: View all (Text/Small: 14px, Gray/Medium) */}
                <IonText color={cardColor} >
                    <p className="ion-no-margin" style={{ fontSize: '14px', lineHeight: '20px', fontWeight: '400' }}>
                        View all
                    </p>
                </IonText>

                {/* Arrow Icon (Gray/Medium) */}
                <IonIcon icon={arrowForward} color={cardColor} size="small" />
            </div>
        </IonCard>
    );
};

export default StatCard;