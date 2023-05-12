-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-05-2023 a las 08:01:52
-- Versión del servidor: 10.4.27-MariaDB
-- Versión de PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sys_health_prueba`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administradores`
--

CREATE TABLE `administradores` (
  `id_administrador` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `id_empleado` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `administradores`
--

INSERT INTO `administradores` (`id_administrador`, `id_empleado`) VALUES
(100281528319213647, 100281528319213645),
(100281528319213651, 100281528319213649),
(100293055843663885, 100293055843663883),
(100301760484081667, 100301760484081665),
(100307794191712258, 100307794191712256);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `citas`
--

CREATE TABLE `citas` (
  `id_cita` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `id_medico` bigint(20) UNSIGNED DEFAULT NULL,
  `id_paciente` bigint(20) UNSIGNED DEFAULT NULL,
  `fecha_hora` datetime NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `citas`
--

INSERT INTO `citas` (`id_cita`, `id_medico`, `id_paciente`, `fecha_hora`, `descripcion`) VALUES
(100297289641230336, 100293055843663878, 100292102226706434, '2023-05-04 11:00:00', 'PRUEBA'),
(100306180491968533, 100303608460869638, 100293055843663873, '2023-05-10 16:30:00', 'PRUEBA'),
(100307818015358984, 100303608460869641, 100293055843663873, '2023-05-12 10:30:00', 'PRUEBA'),
(100307818015358985, 100303608460869641, 100307794191712258, '2023-05-13 12:00:00', 'PRUEBA'),
(100307818015358986, 100303608460869641, 100306180491968538, '2023-05-18 10:00:00', 'PRUEBA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `departamentos`
--

CREATE TABLE `departamentos` (
  `id_departamento` int(11) NOT NULL,
  `nombre_departamento` varchar(50) NOT NULL,
  `descripcion_departamento` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `departamentos`
--

INSERT INTO `departamentos` (`id_departamento`, `nombre_departamento`, `descripcion_departamento`) VALUES
(6, 'Cardiología', 'Departamento encargado de el corazón'),
(7, 'Pediatria', 'prueba'),
(8, 'Oncologia', 'Departamento de prueba'),
(9, 'Dermatología', 'Departamento encargado de la piela');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id_empleado` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `id_departamento` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido_paterno` varchar(50) NOT NULL,
  `apellido_materno` varchar(50) NOT NULL,
  `genero` enum('FEMENINO','MASCULINO') DEFAULT 'MASCULINO',
  `direccion` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo_electronico` varchar(100) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_empleado`, `id_departamento`, `nombre`, `apellido_paterno`, `apellido_materno`, `genero`, `direccion`, `telefono`, `correo_electronico`, `fecha_nacimiento`) VALUES
(100281528319213645, 6, 'Juana Maria', 'Del Campo', 'Rosales', 'MASCULINO', 'Alamedas', '156899', 'sssssss', '1999-06-01'),
(100281528319213649, 6, 'Juana Maria', 'Del Campo', 'Rosales', 'MASCULINO', 'Alamedas', '156899', 'sssssss', '1999-06-01'),
(100293055843663877, 6, 'Juana Maria', 'Del Campo', 'Rosales', 'MASCULINO', 'Alamedas', '156899', 'sssssss', '1999-06-01'),
(100293055843663880, 6, 'Juana Maria', 'Del Campo', 'Rosales', 'MASCULINO', 'Alamedas', '156899', 'sssssss', '1999-06-01'),
(100293055843663883, 6, 'Juana Maria', 'Del Campo', 'Rosales', 'MASCULINO', 'Alamedas', '156899', 'sssssss', '1999-06-01'),
(100300375944331269, 6, 'Juana Maria', 'Del Campo', 'Rosales', 'MASCULINO', 'Alamedas', '156899', 'sssssss', '1999-06-01'),
(100301760484081665, 7, 'Administrador', 'En', 'Prueba', 'MASCULINO', 'Domicilio de prueba', '811299203', 'prueba@correo.com', '2001-06-10'),
(100303608460869632, 7, 'Usuario Recepcion', 'Para', 'Prueba', 'MASCULINO', 'Recepcion', 'Recepcion', 'Recepcion', '2023-05-16'),
(100303608460869637, 7, 'Usuario Medico', 'Para', 'Pruebas', 'MASCULINO', 'Domicilio de pruebas', '911199222', 'medico@correo.com', '2001-10-09'),
(100303608460869640, 7, 'Usuario Medico', 'Para', 'Pruebas', 'MASCULINO', 'Domicilio de pruebas', '911199222', 'medico@correo.com', '2001-10-09'),
(100307794191712256, 9, 'Franco', 'Rios', 'Vargas', 'MASCULINO', 'Guadalupe Nuevo León', '811299330', 'franco@correo.com', '1997-10-01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_medico`
--

CREATE TABLE `historial_medico` (
  `id_historial` bigint(20) NOT NULL DEFAULT uuid_short(),
  `id_paciente` bigint(20) UNSIGNED DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `descripcion` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `historial_medico`
--

INSERT INTO `historial_medico` (`id_historial`, `id_paciente`, `fecha`, `descripcion`) VALUES
(100305252611260416, 100301760484081664, '2023-06-02', 'PRUEBAAA'),
(100306180491968534, 100293055843663873, '2023-05-11', 'PRIMERA ALSKD SKSKS '),
(100307794191712261, 100307794191712258, '2023-05-12', 'PRIMERA CONSULTA'),
(100307794191712267, 100292102226706432, '2023-05-12', 'PADECIMIENTOS DE PRUEBA'),
(100307794191712268, 100292102226706432, '2023-05-12', 'PADECIMIENTOS DE PRUEBA'),
(100307818015358982, 100301760484081664, '2023-06-02', 'PADECIMIENTOS DE PRUEBA'),
(100307818015358988, 100307794191712258, '2023-05-13', 'PADECIMIENTOS'),
(100307818015358991, 100307794191712258, '2023-05-13', 'PADECIMIENTOS DE PRUEBA'),
(100307818015358996, 100293055843663873, '2023-05-12', 'llslslsldldld');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `medicos`
--

CREATE TABLE `medicos` (
  `id_medico` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `especialidad` varchar(50) NOT NULL,
  `cedula_profesional` varchar(20) NOT NULL,
  `id_empleado` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `medicos`
--

INSERT INTO `medicos` (`id_medico`, `especialidad`, `cedula_profesional`, `id_empleado`) VALUES
(100293055843663878, 'Pediatria', 'sssss111w333', 100293055843663877),
(100303608460869638, 'Pediatria', '111999222', 100303608460869637),
(100303608460869641, 'Pediatria', '111999222', 100303608460869637);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes_laboratorio`
--

CREATE TABLE `ordenes_laboratorio` (
  `id_orden` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `id_paciente` bigint(20) UNSIGNED DEFAULT NULL,
  `id_medico` bigint(20) UNSIGNED DEFAULT NULL,
  `fecha` date NOT NULL,
  `tipo_analisis` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ordenes_laboratorio`
--

INSERT INTO `ordenes_laboratorio` (`id_orden`, `id_paciente`, `id_medico`, `fecha`, `tipo_analisis`) VALUES
(100293055843663885, 100292102226706432, 100293055843663878, '2023-05-10', 'Prueba'),
(100306180491968532, 100301760484081664, 100303608460869638, '2023-05-10', 'BIOQUIMICA SANGUINEA DE 4 ELEMENTOS'),
(100306180491968536, 100293055843663873, 100303608460869641, '2023-05-10', 'SIN ANALISIS'),
(100307464670412800, 100301760484081664, 100303608460869638, '2023-05-11', 'SIN ANALISIS'),
(100307464670412802, 100301760484081664, 100303608460869638, '2023-05-11', 'SIN ANALISIS'),
(100307794191712262, 100307794191712258, 100303608460869638, '2023-05-11', 'SIN ANALISIS'),
(100307794191712266, 100292102226706432, 100303608460869638, '2023-05-11', 'ORDEN DE LABORATORIO'),
(100307818015358976, 100292102226706432, 100303608460869638, '2023-05-11', 'SIN ANALISIS'),
(100307818015358978, 100292102226706432, 100303608460869638, '2023-05-11', 'SIN ANALISIS'),
(100307818015358981, 100301760484081664, 100303608460869638, '2023-05-11', 'skdkdkdkdkdkd'),
(100307818015358990, 100307794191712258, 100303608460869641, '2023-05-11', 'PRUEBA\n'),
(100307818015358992, 100293055843663873, 100303608460869641, '2023-05-11', 'SIN ANALISIS'),
(100307818015358994, 100293055843663873, 100303608460869641, '2023-05-11', 'SIN ANALISIS');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pacientes`
--

CREATE TABLE `pacientes` (
  `id_paciente` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `nombre` varchar(50) NOT NULL,
  `apellido_paterno` varchar(50) NOT NULL,
  `apellido_materno` varchar(50) NOT NULL,
  `genero` enum('MASCULINO','FEMENINO') NOT NULL DEFAULT 'MASCULINO',
  `direccion` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo_electronico` varchar(100) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pacientes`
--

INSERT INTO `pacientes` (`id_paciente`, `nombre`, `apellido_paterno`, `apellido_materno`, `genero`, `direccion`, `telefono`, `correo_electronico`, `fecha_nacimiento`) VALUES
(100292102226706432, 'Samuel', 'Amaya', 'ssss', 'MASCULINO', 'Direccion', '811299111', 'correo@samuel.coma', '2001-10-18'),
(100292102226706433, 'Juan', 'Antonio', 'Rolll', 'MASCULINO', 'ssss', '91188299', 'juan@correo.com', '2001-10-03'),
(100292102226706434, 'paciente', 'paciente', 'paciente', 'FEMENINO', 'paciente', '81818292', 'paciente', '2001-10-03'),
(100293055843663873, 'Alejandro', 'Amaya', 'Vazquez', 'MASCULINO', 'Alameda', '1992883', 'sssssssssss', '2003-05-14'),
(100301760484081664, 'Maria', 'Peña', 'Soto', 'MASCULINO', 'Alameda 115', '8119928', 'maria@gmail.com', '1972-03-25'),
(100303608460869635, 'Jose Angel', 'Alvarado', 'Tovanche', 'MASCULINO', 'Guadalupe', '9929393939', 'joseangel@correo.com', '2002-04-08'),
(100303608460869636, 'Javier Alejandro', 'Cardenas', 'Segovia', 'MASCULINO', 'Enrique Segoviano', '8111929292', 'javier@correo.com', '2023-05-08'),
(100306180491968538, 'PACIENTE', 'PARA PROBAR PRUE', 'BDBDBDBD', 'MASCULINO', 'SBDBDBD', 'DFFSDFD', 'DFSDFSDFDS', '2023-05-11'),
(100307794191712258, 'Carlos ', 'Gausin', 'Valero', 'MASCULINO', 'Guadalupe Nuevo León', '1828383838', 'carlos@correo.com', '2002-05-07');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recepcionistas`
--

CREATE TABLE `recepcionistas` (
  `id_recepcionista` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `id_departamento` int(11) NOT NULL,
  `id_empleado` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recepcionistas`
--

INSERT INTO `recepcionistas` (`id_recepcionista`, `id_departamento`, `id_empleado`) VALUES
(100293055843663882, 6, 100293055843663880),
(100300375944331271, 8, 100300375944331269),
(100303608460869634, 7, 100303608460869632);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recetas`
--

CREATE TABLE `recetas` (
  `id_receta` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `id_paciente` bigint(20) UNSIGNED DEFAULT NULL,
  `id_medico` bigint(20) UNSIGNED DEFAULT NULL,
  `fecha` date NOT NULL,
  `diagnostico` varchar(255) NOT NULL,
  `tratamiento` text NOT NULL,
  `id_cita` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recetas`
--

INSERT INTO `recetas` (`id_receta`, `id_paciente`, `id_medico`, `fecha`, `diagnostico`, `tratamiento`, `id_cita`) VALUES
(100307818015358987, 100307794191712258, 100303608460869641, '2023-05-11', 'RECETA', 'RECETA DE PRUEBAS\n', 100307818015358985),
(100307818015358989, 100307794191712258, 100303608460869641, '2023-05-11', 'ALSLSLSL', 'SLSLDLDKD\n', 100307818015358985),
(100307818015358993, 100293055843663873, 100303608460869641, '2023-05-11', 'SIN DIAGNOSTICO', 'SIN TRATAMIENTO', 100307818015358984),
(100307818015358995, 100293055843663873, 100303608460869641, '2023-05-11', 'SIN DIAGNOSTICO', 'SIN TRATAMIENTO', 100307818015358984);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` bigint(20) UNSIGNED NOT NULL DEFAULT uuid_short(),
  `usuario` varchar(50) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `rol` enum('Medico','Administrador','Recepcionista') NOT NULL,
  `id_empleado` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `usuario`, `contrasena`, `rol`, `id_empleado`) VALUES
(100281528319213646, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Administrador', 100281528319213645),
(100281528319213650, 'Ivan', 'cd0b9452fc376fc4c35a60087b366f70d883fc901524daf1f122fbd319384f6a', 'Administrador', 100281528319213649),
(100293055843663879, 'Medico', 'dd7b1b1304303a835688b2ea4c1825c76e748a66c88cebcffd1d983fb8c9dab0', 'Medico', 100293055843663877),
(100293055843663881, 'Juana Maria', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Recepcionista', 100293055843663880),
(100293055843663884, 'admin2', '1c142b2d01aa34e9a36bde480645a57fd69e14155dacfab5a3f9257b77fdc8d8', 'Administrador', 100293055843663883),
(100300375944331270, 'pruebaRecepcion', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'Recepcionista', 100300375944331269),
(100301760484081666, 'admin12', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'Administrador', 100301760484081665),
(100303608460869633, 'recepcion', 'db1963354b34cc7e89e71bfd4fcd5b65d16667b33b82bbe77610f74467a738cd', 'Recepcionista', 100303608460869632),
(100303608460869642, 'medico1', 'dd7b1b1304303a835688b2ea4c1825c76e748a66c88cebcffd1d983fb8c9dab0', 'Medico', 100303608460869637),
(100307794191712257, 'franco', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'Administrador', 100307794191712256);

--
-- Disparadores `usuarios`
--
DELIMITER $$
CREATE TRIGGER `fill_administradores` AFTER INSERT ON `usuarios` FOR EACH ROW IF NEW.rol = 'Administrador' THEN
        INSERT INTO administradores (id_administrador, id_empleado)
        SELECT MAX(id_usuario) + 1, NEW.id_empleado
        FROM usuarios;
    END IF
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `fill_recepcion` AFTER INSERT ON `usuarios` FOR EACH ROW IF NEW.rol = 'Recepcionista' THEN
    INSERT INTO recepcionistas (id_departamento, id_empleado)
    SELECT e.id_departamento, NEW.id_empleado
    FROM empleados e
    WHERE e.id_empleado = NEW.id_empleado;
  END IF
$$
DELIMITER ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD PRIMARY KEY (`id_administrador`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- Indices de la tabla `citas`
--
ALTER TABLE `citas`
  ADD PRIMARY KEY (`id_cita`),
  ADD KEY `id_medico` (`id_medico`),
  ADD KEY `id_paciente` (`id_paciente`);

--
-- Indices de la tabla `departamentos`
--
ALTER TABLE `departamentos`
  ADD PRIMARY KEY (`id_departamento`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id_empleado`),
  ADD KEY `id_departamento` (`id_departamento`);

--
-- Indices de la tabla `historial_medico`
--
ALTER TABLE `historial_medico`
  ADD PRIMARY KEY (`id_historial`),
  ADD KEY `id_paciente` (`id_paciente`);

--
-- Indices de la tabla `medicos`
--
ALTER TABLE `medicos`
  ADD PRIMARY KEY (`id_medico`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- Indices de la tabla `ordenes_laboratorio`
--
ALTER TABLE `ordenes_laboratorio`
  ADD PRIMARY KEY (`id_orden`),
  ADD KEY `id_paciente` (`id_paciente`),
  ADD KEY `id_medico` (`id_medico`);

--
-- Indices de la tabla `pacientes`
--
ALTER TABLE `pacientes`
  ADD PRIMARY KEY (`id_paciente`);

--
-- Indices de la tabla `recepcionistas`
--
ALTER TABLE `recepcionistas`
  ADD PRIMARY KEY (`id_recepcionista`),
  ADD KEY `id_departamento` (`id_departamento`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- Indices de la tabla `recetas`
--
ALTER TABLE `recetas`
  ADD PRIMARY KEY (`id_receta`),
  ADD KEY `id_paciente` (`id_paciente`),
  ADD KEY `id_medico` (`id_medico`),
  ADD KEY `fk_recetas_citas` (`id_cita`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `usuario` (`usuario`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `departamentos`
--
ALTER TABLE `departamentos`
  MODIFY `id_departamento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD CONSTRAINT `administradores_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`);

--
-- Filtros para la tabla `citas`
--
ALTER TABLE `citas`
  ADD CONSTRAINT `citas_ibfk_1` FOREIGN KEY (`id_medico`) REFERENCES `medicos` (`id_medico`),
  ADD CONSTRAINT `citas_ibfk_2` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`);

--
-- Filtros para la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD CONSTRAINT `empleados_ibfk_1` FOREIGN KEY (`id_departamento`) REFERENCES `departamentos` (`id_departamento`),
  ADD CONSTRAINT `fk_dep` FOREIGN KEY (`id_departamento`) REFERENCES `departamentos` (`id_departamento`);

--
-- Filtros para la tabla `historial_medico`
--
ALTER TABLE `historial_medico`
  ADD CONSTRAINT `historial_medico_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`),
  ADD CONSTRAINT `historial_medico_ibfk_2` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`);

--
-- Filtros para la tabla `medicos`
--
ALTER TABLE `medicos`
  ADD CONSTRAINT `medicos_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`);

--
-- Filtros para la tabla `ordenes_laboratorio`
--
ALTER TABLE `ordenes_laboratorio`
  ADD CONSTRAINT `ordenes_laboratorio_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`),
  ADD CONSTRAINT `ordenes_laboratorio_ibfk_2` FOREIGN KEY (`id_medico`) REFERENCES `medicos` (`id_medico`);

--
-- Filtros para la tabla `recepcionistas`
--
ALTER TABLE `recepcionistas`
  ADD CONSTRAINT `recepcionistas_ibfk_1` FOREIGN KEY (`id_departamento`) REFERENCES `departamentos` (`id_departamento`),
  ADD CONSTRAINT `recepcionistas_ibfk_2` FOREIGN KEY (`id_departamento`) REFERENCES `departamentos` (`id_departamento`),
  ADD CONSTRAINT `recepcionistas_ibfk_3` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`);

--
-- Filtros para la tabla `recetas`
--
ALTER TABLE `recetas`
  ADD CONSTRAINT `fk_recetas_citas` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`),
  ADD CONSTRAINT `recetas_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`),
  ADD CONSTRAINT `recetas_ibfk_2` FOREIGN KEY (`id_medico`) REFERENCES `medicos` (`id_medico`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
