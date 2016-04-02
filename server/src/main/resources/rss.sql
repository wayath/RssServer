-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u1build0.15.04.1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Sam 26 Mars 2016 à 02:54
-- Version du serveur :  5.6.28-0ubuntu0.15.04.1
-- Version de PHP :  5.6.4-4ubuntu6.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `rss`
--

CREATE DATABASE IF NOT EXISTS rss;
USE rss;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `type` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `user_auth`
--

CREATE TABLE IF NOT EXISTS `user_auth` (
`id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Structure de la table `feed`
--

CREATE TABLE IF NOT EXISTS `feed` (
`id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Structure de la table `article`
--

CREATE TABLE IF NOT EXISTS `article` (
`id` int(11) NOT NULL,
  `id_feed` int(11) NOT NULL,
  `link` varchar(255) NOT NULL,
  `title` varchar(500) NOT NULL,
  `preview` varchar(500) NOT NULL,
  `full_content` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `pub_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Structure de la table `article_state`
--

CREATE TABLE IF NOT EXISTS `article_state` (
`id` int(11) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `article_state`
--

INSERT INTO `article_state` (`id`, `label`, `status`) VALUES
(1, 'new', 0),
(2, 'read', 1);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `username` (`username`);

--
-- Index pour la table `user_auth`
--
ALTER TABLE `user_auth`
 ADD PRIMARY KEY (`id`), ADD KEY `id_user` (`id_user`);

--
-- Index pour la table `feed`
--
ALTER TABLE `feed`
 ADD PRIMARY KEY (`id`), ADD KEY `id_user` (`id_user`);

--
-- Index pour la table `article`
--
ALTER TABLE `article`
 ADD PRIMARY KEY (`id`), ADD KEY `status` (`status`), ADD KEY `id_feed` (`id_feed`);

--
-- Index pour la table `article_state`
--
ALTER TABLE `article_state`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `state` (`status`), ADD KEY `status` (`status`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `user_auth`
--
ALTER TABLE `user_auth`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `feed`
--
ALTER TABLE `feed`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `article`
--
ALTER TABLE `article`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `article_state`
--
ALTER TABLE `article_state`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `user_auth`
--
ALTER TABLE `user_auth`
ADD CONSTRAINT `user_auth_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);
--
-- Contraintes pour la table `feed`
--
ALTER TABLE `feed`
ADD CONSTRAINT `feed_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);
--
-- Contraintes pour la table `article`
--
ALTER TABLE `article`
ADD CONSTRAINT `article_ibfk_1` FOREIGN KEY (`id_feed`) REFERENCES `feed` (`id`),
ADD CONSTRAINT `article_ibfk_2` FOREIGN KEY (`status`) REFERENCES `article_state` (`status`);
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
