# Snow Sense (frontend readme) 🎿

A full-stack mobile application built with **Ionic React** and **Spring Boot** that provides real-time ski resort information, including avalanche forecasts, lift availability, slope status, and weather conditions.

## 📱 Features

* **Live Dashboard:** View global lift statistics and personalized resort recommendations based on your location.
* **Resort List:** Dynamic list of resorts displaying live temperature and open lift/slope counts.
* **Smart Search:** Unified search bar that filters through resorts, specific lifts, and slopes instantly.
* **Resort Details:** Detailed view for every resort showing avalanche danger levels, specific lift status, and slope difficulty.
* **Geolocation:** Automatically finds and recommends the nearest resort to you that has open lifts.

## 🛠️ Tech Stack

* **Frontend:** Ionic Framework 7, React, TypeScript
* **Backend:** Java Spring Boot
* **State Management:** React Context API
* **Icons:** Ionicons & React Icons (`fa`)
---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:
* **Node.js** (v14 or higher)
* **Java JDK** (v17 or higher)
* **Ionic CLI:** `npm install -g @ionic/cli`

---

### 1. Backend Setup (Spring Boot)

The frontend relies on the backend running on port `8080`.

1.  **Navigate** to your backend project folder.
2.  **Configure CORS:** Ensure your `WebConfig.java` allows requests from the frontend (usually port `8100` for Ionic).

    ```java
    // src/main/java/com/notification/config/WebConfig.java
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:8100", "http://localhost:5173") // Add your frontend ports
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        }
    }
    ```
3.  **Run the Server:**
    ```bash
    ./mvnw spring-boot:run
    ```

### 2. Frontend Setup (Ionic)

1.  **Navigate** to your frontend project folder.
2.  **Install Dependencies:**
    ```bash
    npm install
    npm install react-icons
    ```
3.  **Run the App:**
    ```bash
    ionic serve
    ```
    This will launch the app in your browser at `http://localhost:8100`.

---

## 📡 API Architecture

The application uses a **Context API (`ResortDataContext`)** to fetch data once on startup to reduce server load. It expects the following endpoints at `http://localhost:8080`:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/resorts/with-avalanche` | Main list of resorts with safety ratings. |
| `GET` | `/api/resorts/{id}/with-avalanche` | Specific details (Danger level, tendency) for a resort. |
| `GET` | `/api/skiresort/lifts` | List of all lifts across all resorts. |
| `GET` | `/api/skiresort/slopes` | List of all slopes across all resorts. |
| `GET` | `/api/weather` | Current temperature data. |

---

## ⚠️ Troubleshooting

**1. "Network Error" or Empty Data**
* Ensure the Spring Boot server is running on port 8080.
* Check your browser console (F12). If you see **CORS** errors, verify the `WebConfig.java` in your backend includes `http://localhost:8100`.

**2. Running on Android Emulator**
* If running on an Android emulator via Capacitor, `localhost` refers to the emulator itself. You must change the API URL in `src/context/ResortDataContext.tsx`:
    * **Change:** `http://localhost:8080/...`
    * **To:** `http://10.0.2.2:8080/...`

**3. Geolocation not working**
* The recommendation feature requires location permissions. Ensure your browser or device allows location access for the app.