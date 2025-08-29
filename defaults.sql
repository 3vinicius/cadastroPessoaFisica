SET TIME ZONE 'America/Sao_Paulo';

DROP TABLE IF EXISTS clientes;


--------------------------- CRIACAO DA TABELA

CREATE TABLE clientes (
                               id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL,
                               nome VARCHAR(150) NOT NULL,
                               idade INT ,
                               cpf CHAR(12) NOT NULL UNIQUE,
                               data_nascimento DATE NOT NULL,
                               email VARCHAR(120) UNIQUE NOT NULL,
                               telefone VARCHAR(24) NOT NULL,
                               endereco VARCHAR(255) NOT NULL ,
                               cep VARCHAR(9) NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

--------------------------- DEFAULT CARGA DE DADOS


INSERT INTO clientes (
    nome, email, endereco, cep, telefone, cpf, data_nascimento, created_at, updated_at, idade
)
SELECT
    'Cliente ' || i,
    'cliente' || i || '@gmail.com',
    'Endereço ' || i,
    '57200-' || lpad(i::text, 3, '0'),
    '+55 (82) 9 9999-' || lpad(i::text, 4, '0'),
    lpad(i::text, 4, '0') || '67976-05',
    data_nasc,
    now(),
    now(),
    date_part('year', age(data_nasc))::int
FROM (
         SELECT
             i,
             '1980-01-01'::date + (random() * 16000)::int AS data_nasc
         FROM generate_series(1, 900) AS s(i)
     ) AS t;



--------------------------- CRIANDO USUARIO PARA ACESSO AO BANCO

DROP TABLE IF EXISTS usuarios;


CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY UNIQUE NOT NULL ,
                          email VARCHAR(120) UNIQUE NOT NULL,
                          senha VARCHAR(120)  NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


