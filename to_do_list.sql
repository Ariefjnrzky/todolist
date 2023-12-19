-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 18 Des 2023 pada 16.51
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `to_do_list`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal`
--

CREATE TABLE `jadwal` (
  `IDJadwal` int(11) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Hari` varchar(11) NOT NULL,
  `Waktu` varchar(11) NOT NULL,
  `Kegiatan` varchar(255) NOT NULL,
  `Status` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `jadwal`
--

INSERT INTO `jadwal` (`IDJadwal`, `Username`, `Hari`, `Waktu`, `Kegiatan`, `Status`) VALUES
(6, 'tama', 'Senin', '07.00-09.15', 'MTK', 1),
(8, 'tono', 'Senin', '01.00-01.15', 'Tahajud', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tugas`
--

CREATE TABLE `tugas` (
  `IDTugas` int(10) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Date` date NOT NULL,
  `Deadline` varchar(5) NOT NULL,
  `Tugas` varchar(255) NOT NULL,
  `Status` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tugas`
--

INSERT INTO `tugas` (`IDTugas`, `Username`, `Date`, `Deadline`, `Tugas`, `Status`) VALUES
(1, 'tama', '2023-12-05', '09.00', 'MTK', 1),
(2, 'tama', '2023-12-20', '09.00', 'IPA', 0),
(3, 'tama', '2023-12-20', '09.00', 'IPS', 0),
(4, 'tama', '2023-12-22', '09.00', 'PPKN', 0),
(5, 'tama', '2023-12-21', '09.00', 'MTK1', 0),
(7, 'tama', '2023-12-05', '12.00', 'Merangkum Buku', 0),
(8, 'tono', '2023-12-29', '00.00', 'MTK', 0),
(9, 'tama', '2023-12-29', '10.00', 'Bahasa', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `NamaLengkap` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`Username`, `Password`, `NamaLengkap`, `Email`) VALUES
('tama', 'tama', 'Tama Robert', 'tama@gmail.com'),
('tono', 'tono', 'tono', 'tono@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`IDJadwal`),
  ADD KEY `jadwal_ibfk_1` (`Username`);

--
-- Indeks untuk tabel `tugas`
--
ALTER TABLE `tugas`
  ADD PRIMARY KEY (`IDTugas`),
  ADD KEY `tugas_ibfk_1` (`Username`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `jadwal`
--
ALTER TABLE `jadwal`
  MODIFY `IDJadwal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `tugas`
--
ALTER TABLE `tugas`
  MODIFY `IDTugas` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `jadwal_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `users` (`Username`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Ketidakleluasaan untuk tabel `tugas`
--
ALTER TABLE `tugas`
  ADD CONSTRAINT `tugas_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `users` (`Username`) ON DELETE CASCADE ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
