-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le : lun. 14 avr. 2025 à 15:04
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `attraction`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `idAdmin` int NOT NULL AUTO_INCREMENT,
  `mail` varchar(100) NOT NULL,
  `motDePasse` varchar(20) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idAdmin`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`idAdmin`, `mail`, `motDePasse`, `nom`, `prenom`) VALUES
(1, 'amandine.soyez@edu.ece.fr', 'Foxie274', 'Soyez', 'Amandine'),
(2, 'pacome.golvet@edu.ece.fr', 'Linette', 'Golvet', 'Pacome'),
(3, 'valentin.knockaert@edu.ece.fr', 'Dean', 'Knockaert', 'Valentin'),
(4, 'martin.duverneuil@edu.ece.fr', 'motdepasse', 'Duverneuil', 'Martin'),
(5, 'yossra.hajjaji@edu.ece.fr', 'jadorelejava', 'Hajjaji', 'Yossra'),
(6, 'steven.spielberg@edu.ece.fr', 'Dino', 'Spielberg', 'Steven');

-- --------------------------------------------------------

--
-- Structure de la table `attraction`
--

DROP TABLE IF EXISTS `attraction`;
CREATE TABLE IF NOT EXISTS `attraction` (
  `idAttraction` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prix` decimal(10,2) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idAttraction`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `attraction`
--

INSERT INTO `attraction` (`idAttraction`, `nom`, `prix`, `image`) VALUES
(1, 'Parcours Jurassic Guidé', '15.75', 'pacoursJurassicGuide.png'),
(2, 'Tour en Safari', '10.25', 'tourEnSafari.png'),
(3, 'Croisière de la Jungle', '10.25', 'croisiereDeLaJungle.png'),
(4, 'Le Lagon', '7.80', 'leLagon.png'),
(5, 'La Volière', '7.25', 'laVoliere.png'),
(6, 'Roller Coaster : Bone Shaker', '5.60', 'rollerCoaster.png');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `idClient` int NOT NULL AUTO_INCREMENT,
  `mail` varchar(100) NOT NULL,
  `motDePasse` varchar(20) NOT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `dateDeNaissance` date DEFAULT NULL,
  PRIMARY KEY (`idClient`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`idClient`, `mail`, `motDePasse`, `prenom`, `nom`, `dateDeNaissance`) VALUES
(1, 'lola.coignard@gmail.com', 'triceratops', 'Lola', 'Coignard', '2003-12-07'),
(2, 'linette.golvet@gmail.com', 'Meow', 'Linette', 'Golvet', '2006-01-01'),
(3, 'galaad.soyez@gmail.com', 'Miaou', 'Galaad', 'Soyez', '2017-06-24'),
(4, 'jensen.ackles@gmail.com', 'castiel', 'Jensen', 'Ackles', '1978-03-01'),
(5, 'rohr.soyez-golvet@gmail.com', 'iule', 'Rohr', 'Soyez-Golvet', '2024-02-22'),
(6, 'ian.malcolm@gmail.com', 'chaos', 'Ian', 'Malcolm', '1967-09-21');

-- --------------------------------------------------------

--
-- Structure de la table `evenement`
--

DROP TABLE IF EXISTS `evenement`;
CREATE TABLE IF NOT EXISTS `evenement` (
  `idEvenement` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `supplement` decimal(10,2) DEFAULT NULL,
  `dateDebut` date DEFAULT NULL,
  `dateFin` date DEFAULT NULL,
  PRIMARY KEY (`idEvenement`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `evenement`
--

INSERT INTO `evenement` (`idEvenement`, `nom`, `image`, `supplement`, `dateDebut`, `dateFin`) VALUES
(1, 'Noël', 'noel.png', '5.00', '2025-12-20', '2025-12-27'),
(2, 'Nouvel An', 'nouvelAn.png', '5.00', '2025-12-28', '2026-01-03'),
(3, 'Terreur du Passé', 'halloween.png', '2.50', '2025-10-24', '2025-11-02'),
(4, 'Semaine de la Science', 'semaineDeLaScience', '0.00', '2025-05-05', '2025-05-11'),
(5, 'Chasse aux oeufs ', 'chasseAuxOeufs.png', '1.75', '2025-04-17', '2025-04-20'),
(6, 'Spectacle Nocturne', 'spectacleNocturne.png', '3.80', '2025-07-02', '2025-08-31');

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

DROP TABLE IF EXISTS `facture`;
CREATE TABLE IF NOT EXISTS `facture` (
  `idFacture` int NOT NULL AUTO_INCREMENT,
  `idClient` int NOT NULL,
  `dateFacture` date DEFAULT NULL,
  `prix` double DEFAULT NULL,
  PRIMARY KEY (`idFacture`),
  KEY `idClient` (`idClient`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `idReservation` int NOT NULL AUTO_INCREMENT,
  `idClient` int NOT NULL,
  `idAttraction` int NOT NULL,
  `dateAttraction` date DEFAULT NULL,
  `dateReservation` date DEFAULT NULL,
  `idFacture` int DEFAULT '0',
  PRIMARY KEY (`idReservation`),
  KEY `idClient` (`idClient`),
  KEY `idAttraction` (`idAttraction`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`idReservation`, `idClient`, `idAttraction`, `dateAttraction`, `dateReservation`, `idFacture`) VALUES
(2, 1, 5, '2025-04-03', '2025-04-14', 0),
(3, 1, 3, '2025-04-11', '2025-04-14', 0),
(4, 1, 4, '2025-04-04', '2025-04-14', 0),
(5, 1, 1, '2025-04-14', '2025-04-14', 0),
(6, 1, 1, '2025-04-04', '2025-04-14', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
