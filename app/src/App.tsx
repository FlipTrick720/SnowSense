import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

import '@ionic/react/css/core.css';

import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/**
 * Ionic Dark Mode
 * -----------------------------------------------------
 * For more info, please see:
 * https://ionicframework.com/docs/theming/dark-mode
 */

/* import '@ionic/react/css/palettes/dark.always.css'; */
/* import '@ionic/react/css/palettes/dark.class.css'; */
import '@ionic/react/css/palettes/dark.system.css';

/* Theme variables */
import './theme/variables.css';
import Tabs from "./pages/Tabs";
import React from "react";
import ResortPage from "./pages/ResortPage"; 
import { ResortDataProvider } from './context/ResortDataContext';
setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <ResortDataProvider>
      <IonReactRouter>
        <IonRouterOutlet>
          
          <Route 
            path="/resort/:id" 
            render={(props) => <ResortPage {...props} />}
            exact={true}
          />

          <Route path="/app" component={Tabs} />
          
          <Route exact path="/">
            <Redirect to="/app/home" />
          </Route>

        </IonRouterOutlet>
      </IonReactRouter>
    </ResortDataProvider>
  </IonApp>
);

export default App;
