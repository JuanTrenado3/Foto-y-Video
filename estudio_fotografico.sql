-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 13-06-2026 a las 19:43:03
-- Versión del servidor: 8.4.3
-- Versión de PHP: 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `estudio_fotografico`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignaciones`
--

CREATE TABLE `asignaciones` (
  `id_asignacion` int NOT NULL,
  `id_contrato` int NOT NULL,
  `id_empleado` int NOT NULL,
  `labor_asignada` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contratos`
--

CREATE TABLE `contratos` (
  `id_contrato` int NOT NULL,
  `nombre_cliente` varchar(100) NOT NULL,
  `nombre_festejado` varchar(100) DEFAULT NULL,
  `tel_cliente` varchar(20) NOT NULL,
  `dir_misa` varchar(255) DEFAULT NULL,
  `dir_fiesta` varchar(255) DEFAULT NULL,
  `fecha_evento` date NOT NULL,
  `hora_misa` time DEFAULT NULL,
  `hora_fiesta` time DEFAULT NULL,
  `id_paquete` int NOT NULL,
  `costo_total` decimal(10,2) NOT NULL,
  `anticipo_pagado` decimal(10,2) NOT NULL,
  `saldo_restante` decimal(10,2) GENERATED ALWAYS AS ((`costo_total` - `anticipo_pagado`)) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `contratos`
--

INSERT INTO `contratos` (`id_contrato`, `nombre_cliente`, `nombre_festejado`, `tel_cliente`, `dir_misa`, `dir_fiesta`, `fecha_evento`, `hora_misa`, `hora_fiesta`, `id_paquete`, `costo_total`, `anticipo_pagado`) VALUES
(1, 'Tony', 'Lute', '444', 'Santo Ecceomo esquina primera de mayo 98', 'San Isidro Labrador 23', '2026-06-21', '12:00:00', '16:00:00', 1, 18000.00, 10000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id_empleado` int NOT NULL,
  `nombre_completo` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `id_rol` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_empleado`, `nombre_completo`, `telefono`, `password`, `id_rol`) VALUES
(6, 'Juan', '1', '123', 1),
(7, 'Sebastián', '2', '123', 2),
(8, 'Diego', '3', '123', 3),
(9, 'Tony', '4', '123', 4),
(10, 'Trenado', '5', '123', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paquetes`
--

CREATE TABLE `paquetes` (
  `id_paquete` int NOT NULL,
  `categoria_evento` varchar(50) NOT NULL,
  `nombre_paquete` varchar(50) NOT NULL,
  `num_fotos` int DEFAULT '0',
  `incluye_video` tinyint(1) DEFAULT '0',
  `incluye_sesion` tinyint(1) DEFAULT '0',
  `precio_base` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `paquetes`
--

INSERT INTO `paquetes` (`id_paquete`, `categoria_evento`, `nombre_paquete`, `num_fotos`, `incluye_video`, `incluye_sesion`, `precio_base`) VALUES
(1, 'Boda', 'Paquete Boda Premium (Prueba)', 100, 1, 1, 8500.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` int NOT NULL,
  `nombre_rol` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre_rol`, `descripcion`) VALUES
(1, 'Administrador', 'Dueño del negocio, control total y gestión de empleados'),
(2, 'Empleado de Local', 'Atención al cliente, creación de contratos y asignación de personal'),
(3, 'Fotógrafo', 'Personal técnico, solo toma fotografías'),
(4, 'Camarógrafo', 'Personal técnico, solo graba video'),
(5, 'Híbrido', 'Personal técnico capacitado para foto y video');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asignaciones`
--
ALTER TABLE `asignaciones`
  ADD PRIMARY KEY (`id_asignacion`),
  ADD KEY `fk_contrato` (`id_contrato`),
  ADD KEY `fk_empleado` (`id_empleado`);

--
-- Indices de la tabla `contratos`
--
ALTER TABLE `contratos`
  ADD PRIMARY KEY (`id_contrato`),
  ADD KEY `fk_paquete` (`id_paquete`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id_empleado`),
  ADD KEY `fk_rol` (`id_rol`);

--
-- Indices de la tabla `paquetes`
--
ALTER TABLE `paquetes`
  ADD PRIMARY KEY (`id_paquete`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asignaciones`
--
ALTER TABLE `asignaciones`
  MODIFY `id_asignacion` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `contratos`
--
ALTER TABLE `contratos`
  MODIFY `id_contrato` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id_empleado` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `paquetes`
--
ALTER TABLE `paquetes`
  MODIFY `id_paquete` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asignaciones`
--
ALTER TABLE `asignaciones`
  ADD CONSTRAINT `fk_contrato` FOREIGN KEY (`id_contrato`) REFERENCES `contratos` (`id_contrato`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`);

--
-- Filtros para la tabla `contratos`
--
ALTER TABLE `contratos`
  ADD CONSTRAINT `fk_paquete` FOREIGN KEY (`id_paquete`) REFERENCES `paquetes` (`id_paquete`);

--
-- Filtros para la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD CONSTRAINT `fk_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
