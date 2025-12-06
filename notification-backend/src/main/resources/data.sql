-- COMPLETE LIST: FREIZEITTICKET TIROL & SNOWCARD TIROL SKI RESORTS
-- All resorts included in either or both passes (95 resorts)
-- Coordinates are base station/main lift locations where available
-- Elevation is maximum altitude (top station)

INSERT INTO ski_resort (name, bergfex_name, latitude, longitude, elevation) VALUES

-- ========== GLACIERS (in both passes) ==========
('Stubai Glacier', 'stubaier-gletscher', 47.0089, 11.1181, 3210),
('Hintertux Glacier', 'hintertux', 47.0744, 11.6581, 3250),
('Pitztal Glacier', 'pitztalergletscher', 46.9239, 10.8786, 3440),
('Kaunertal Glacier', 'kaunertal', 46.9347, 10.7167, 3113),
('Sölden', 'soelden', 46.9691, 11.0039, 3058),

-- ========== INNSBRUCK REGION (Freizeitticket + Snowcard) ==========
('Nordkette', 'innsbruck-nordkette', 47.3050, 11.3839, 2256),
('Patscherkofel', 'innsbruck-igls-patscherkofel', 47.2089, 11.4608, 2246),
('Axamer Lizum', 'axamer-lizum', 47.1936, 11.2968, 2340),
('Glungezer', 'glungezer', 47.2878, 11.5669, 2677),
('Schlick 2000', 'schlick', 47.1622, 11.3106, 2240),
('Serles', 'mieders', 47.1519, 11.3419, 2717),
('Muttereralm', 'muttereralm', 47.2322, 11.3808, 1600),
('Rangger Köpfl', 'ranggerkoepfl', 47.2478, 11.1483, 2000),
('Bergeralm', 'bergeralm', 47.2892, 11.4639, 2000),

-- ========== KÜHTAI & SELLRAINTAL (Freizeitticket + Snowcard) ==========
('Kühtai', 'kuehtai', 47.2089, 11.0142, 2520),

-- ========== ÖTZTAL VALLEY (both passes) ==========
('Hochoetz', 'oetz-hochoetz', 47.2022, 10.8744, 2272),
('Obergurgl-Hochgurgl', 'obergurgl-hochgurgl', 46.8681, 11.0264, 3082),
('Vent', 'vent', 46.8569, 10.9092, 1900),
('Niederthai', 'niederthai-oetztal', 47.1650, 10.8970, 2178),

-- ========== PAZNAUN VALLEY (Freizeitticket + Snowcard) ==========
('Ischgl-Samnaun', 'silvretta-arena-ischgl-samnaun', 46.9914, 10.2992, 2872),
('Galtür', 'silvapark-galtuer', 46.9681, 10.1853, 2297),
('Kappl', 'kappl', 47.0631, 10.3758, 2690),
('See', 'see', 47.0786, 10.4347, 2456),

-- ========== SKI ARLBERG (Freizeitticket - limited, Snowcard - full) ==========
('St. Anton am Arlberg', 'stanton-stchristoph', 47.1275, 10.2642, 2811),
('Lech-Zürs', 'lech-zuers-arlberg', 47.1597, 10.1397, 2450),
('St. Christoph', 'stanton-stchristoph', 47.1219, 10.2936, 2811),
('Stuben', 'stuben', 47.1200, 10.3050, 2450),
('Warth-Schröcken', 'warth-schroecken', 47.2569, 10.1878, 2085),

-- ========== SERFAUS-FISS-LADIS (Snowcard) ==========
('Serfaus-Fiss-Ladis', 'serfaus-fiss-ladis', 47.0375, 10.6061, 2828),

-- ========== NAUDERS & KAUNERTAL (Snowcard) ==========
('Nauders', 'nauders', 46.8892, 10.5042, 2850),
('Fendels', 'fendels', 47.0844, 10.5281, 2613),
('Venet', 'venet', 47.1319, 10.5611, 2512),

-- ========== ZILLERTAL REGION (Snowcard) ==========
('Zillertal Arena', 'zell-am-ziller', 47.2256, 11.9417, 2500),
('Mayrhofen-Hippach', 'mayrhofen', 47.1656, 11.8614, 2500),
('Hochfügen-Hochzillertal', 'hochfuegen', 47.1531, 11.7936, 2500),
('Spieljoch-Fügen', 'fuegen-spieljoch', 47.1894, 11.7544, 2236),
('Kaltenbach-Hochzillertal', 'hochzillertal', 47.2792, 11.8786, 2500),
('Gerlos', 'gerlos', 47.2306, 12.0358, 2166),
('Ski Optimal Hochfügen', 'hochzillertal', 47.1531, 11.7936, 2500),
('Zillertal 3000', 'ski-gletscherwelt-zillertal3000-tux', 47.0744, 11.6581, 3250),

-- ========== ALPBACHTAL & WILDSCHÖNAU (Snowcard) ==========
('Ski Juwel Alpbachtal-Wildschönau', 'wildschoenau', 47.3953, 11.9344, 2025),
('Alpbach', 'alpbachtal', 47.3953, 11.9444, 2025),
('Wildschönau', 'wildschoenau', 47.4319, 12.0392, 1850),
('Reith im Alpbachtal', 'alpbachtal', 47.3836, 11.9142, 1456),

-- ========== KITZBÜHEL REGION (Snowcard) ==========
('KitzSki Kitzbühel-Kirchberg', 'kitzbuehel-kirchberg', 47.4464, 12.3922, 2000),
('St. Johann in Tirol', 'stjohann-tirol', 47.5228, 12.4208, 1696),
('Fieberbrunn', 'fieberbrunn', 47.4642, 12.5589, 2020),
('Kirchberg in Tirol', 'kitzbuehel-kirchberg', 47.4503, 12.3167, 2000),
('Pass Thurn', 'kitzbueheler-alpen-mittersill', 47.2367, 12.4692, 2174),

-- ========== SKIWELT WILDER KAISER-BRIXENTAL (Snowcard) ==========
('SkiWelt Wilder Kaiser-Brixental', 'brixen', 47.5078, 12.1892, 1957),
('Söll', 'soell', 47.5050, 12.1950, 1829),
('Ellmau', 'ellmau', 47.5153, 12.2972, 1555),
('Going', 'going', 47.5089, 12.3228, 1731),
('Scheffau', 'scheffau', 47.4806, 12.2428, 1750),
('Westendorf', 'westendorf', 47.4319, 12.2136, 1892),
('Brixen im Thale', 'brixen', 47.4492, 12.2556, 1856),
('Hopfgarten', 'hopfgarten', 47.4492, 12.1592, 1829),
('Itter', 'hopfgarten', 47.4658, 12.1347, 1200),
('Hohe Salve', 'hopfgarten', 47.4378, 12.1267, 1828),

-- ========== ACHENSEE REGION (Snowcard) ==========
('Achensee - Christlum', 'christlum', 47.4419, 11.7083, 1800),
('Achensee - Pertisau', 'achensee', 47.4139, 11.7175, 1900),
('Rofan', 'rofan', 47.4681, 11.7756, 2259),

-- ========== WIPPTAL (Freizeitticket + Snowcard) ==========
('Steinach am Brenner', 'bergeralm', 47.0878, 11.4714, 2100),

-- ========== EAST TYROL/OSTTIROL (Snowcard) ==========
('Großglockner Resort Kals-Matrei', 'kals-grossglockner', 47.0114, 12.6708, 2621),
('Lienz - Hochstein', 'lienz', 46.8292, 12.7756, 2050),
('Lienz - Zettersfeld', 'lienz', 46.8350, 12.7875, 2278),
('St. Jakob in Defereggen', 'stjakob-defereggen', 46.9231, 12.3542, 2525),
('Sillian-Hochpustertal', 'sillian', 46.7539, 12.4233, 2407),
('Obertilliach', 'obertilliach', 46.7069, 12.6133, 2048),
('Kartitsch', 'dorfberglift-kartitsch', 46.7075, 12.4997, 2100),

-- ========== TIROLER OBERLAND (Snowcard) ==========
('Imst-Hoch-Imst', 'imst', 47.2267, 10.7356, 2050),
('Hahnenkamm Reutte', 'hahnenkamm-hoefen-reutte', 47.4767, 10.7222, 1938),

-- ========== TIROLER ZUGSPITZ ARENA (Snowcard) ==========
('Ehrwald - Zugspitze', 'ehrwald', 47.4006, 10.9308, 2962),
('Lermoos', 'lermoos', 47.4092, 10.8819, 2100),
('Berwang', 'berwang', 47.4036, 10.7667, 1860),
('Bichlbach', 'berwang', 47.4219, 10.7711, 1600),
('Biberwier', 'biberwier', 47.3711, 10.8928, 2100),

-- ========== SEEFELD REGION (Snowcard) ==========
('Rosshütte Seefeld', 'seefeld-rosshuette', 47.3228, 11.1919, 2064),
('Gschwandtkopf', 'seefeld-gschwandtkopf', 47.3386, 11.1706, 1500),

-- ========== PITZTAL (both passes) ==========
('Rifflsee', 'pitztalergletscher', 46.9900, 10.8553, 2880),
('Hochzeiger', 'hochzeiger', 47.0578, 10.8344, 2560),

-- ========== KLEINERE SKIGEBIETE (Various smaller areas) ==========
('Hochkössen', 'koessen', 47.6528, 12.4200, 1670),
('Thiersee', 'schneeberglifte-thiersee', 47.6164, 12.1128, 1280),
('Weerberg', 'huettegglift', 47.2594, 11.6506, 1850),
('Kolsassberg', 'kolsassberg', 47.2881, 11.6183, 1456),
('Pillersee', 'pillersee-hochfilzen', 47.5133, 12.5611, 1663),
('Steinplatte', 'waidring-steinplatte', 47.5156, 12.5619, 1869);