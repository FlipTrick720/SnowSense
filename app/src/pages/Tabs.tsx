import React from 'react';
import { IonTabs, IonTabBar, IonTabButton, IonIcon, IonLabel, IonRouterOutlet } from '@ionic/react';
import { Route, Redirect } from 'react-router-dom';
import { home, search, settings, star } from 'ionicons/icons';

import HomePage from './HomePage';
import SearchPage from './SearchPage';
import SettingsPage from './SettingsPage';
import RecommendationsPage from './RecommendationsPage';

const Tabs: React.FC = () => {
    return (
        <IonTabs>
            <IonRouterOutlet>
                <Redirect exact path="/app" to="/app/home" />
                <Route exact path="/app/home">
                    <HomePage />
                </Route>

                <Route exact path="/app/search">
                    <SearchPage />
                </Route>

                <Route exact path="/app/recommendations">
                    <RecommendationsPage />
                </Route>

                <Route exact path="/app/settings">
                    <SettingsPage />
                </Route>

            </IonRouterOutlet>

            <IonTabBar slot="bottom">
               <IonTabButton tab="home" href="/app/home">
                    <IonIcon icon={home} />
                    <IonLabel>Home</IonLabel>
                </IonTabButton>

                <IonTabButton tab="recommendations" href="/app/recommendations">
                    <IonIcon icon={star} />
                    <IonLabel>Recommendations</IonLabel>
                </IonTabButton>

                <IonTabButton tab="search" href="/app/search">
                    <IonIcon icon={search} />
                    <IonLabel>Search</IonLabel>
                </IonTabButton>

                <IonTabButton tab="settings" href="/app/settings">
                    <IonIcon icon={settings} />
                    <IonLabel>Settings</IonLabel>
                </IonTabButton>

            </IonTabBar>
        </IonTabs>
    );
};

export default Tabs;