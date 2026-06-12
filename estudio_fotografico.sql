CREATE DATABASE IF NOT EXISTS estudio_fotografico;
USE estudio_fotografico;

CREATE TABLE Roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE Empleados (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES Roles(id_rol)
);

CREATE TABLE Paquetes (
    id_paquete INT AUTO_INCREMENT PRIMARY KEY,
    categoria_evento VARCHAR(50) NOT NULL,
    nombre_paquete VARCHAR(50) NOT NULL,
    num_fotos INT DEFAULT 0,
    incluye_video BOOLEAN DEFAULT FALSE,
    incluye_sesion BOOLEAN DEFAULT FALSE,
    precio_base DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Contratos (
    id_contrato INT AUTO_INCREMENT PRIMARY KEY,
    nombre_cliente VARCHAR(100) NOT NULL,
    nombre_festejado VARCHAR(100),
    tel_cliente VARCHAR(20) NOT NULL,
    dir_misa VARCHAR(255),
    dir_fiesta VARCHAR(255),
    fecha_evento DATE NOT NULL,
    hora_misa TIME,
    hora_fiesta TIME,
    id_paquete INT NOT NULL,
    costo_total DECIMAL(10, 2) NOT NULL,
    anticipo_pagado DECIMAL(10, 2) NOT NULL,
    saldo_restante DECIMAL(10, 2) GENERATED ALWAYS AS (costo_total - anticipo_pagado) STORED,
    CONSTRAINT fk_paquete FOREIGN KEY (id_paquete) REFERENCES Paquetes(id_paquete)
);

CREATE TABLE Asignaciones (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_contrato INT NOT NULL,
    id_empleado INT NOT NULL,
    labor_asignada VARCHAR(50) NOT NULL,
    CONSTRAINT fk_contrato FOREIGN KEY (id_contrato) REFERENCES Contratos(id_contrato) ON DELETE CASCADE,
    CONSTRAINT fk_empleado FOREIGN KEY (id_empleado) REFERENCES Empleados(id_empleado)
);