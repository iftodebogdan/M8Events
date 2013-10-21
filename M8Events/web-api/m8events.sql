-- phpMyAdmin SQL Dump
-- version 3.4.11.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 21, 2013 at 05:26 PM
-- Server version: 5.5.23
-- PHP Version: 5.3.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `addict_m8events`
--
CREATE DATABASE `m8events` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `m8events`;

-- --------------------------------------------------------

--
-- Table structure for table `events_list`
--

CREATE TABLE IF NOT EXISTS `events_list` (
  `event_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `event_summary_description` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `event_detailed_description` text COLLATE utf8_unicode_ci NOT NULL,
  `event_date` date NOT NULL,
  `event_time` time NOT NULL,
  `event_location_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `event_location_coords` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=19 ;

--
-- Dumping data for table `events_list`
--

INSERT INTO `events_list` (`event_id`, `event_name`, `event_summary_description`, `event_detailed_description`, `event_date`, `event_time`, `event_location_name`, `event_location_coords`) VALUES
(18, 'test '' test', 'test '' test', 'test '' test', '2014-02-16', '13:42:00', 'test '' test', 'test '' test'),
(15, 'Numele evenimentului in maxim 64 de caractere', 'O descriere sumara a evenimentului in maxim 128 de caractere care poate cuprinde data, ora si locatia evenimentului', 'O descriere detaliata a evenimentului. Acest camp accepta mult text.', '2014-02-16', '13:42:00', 'Numele locatiei in maxim 64 de caractere', '44.415107,26.107035');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
