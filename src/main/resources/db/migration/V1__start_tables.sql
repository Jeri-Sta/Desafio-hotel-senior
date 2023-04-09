CREATE TABLE IF NOT EXISTS public.check_in
(
       id bigserial not null,
        adiciona_veiculo boolean not null,
        data_entrada timestamp(6),
        data_saida timestamp(6),
        valor_total_hospedagem numeric(38,2),
        hospede_id bigint not null,
        primary key (id)
); 

CREATE TABLE IF NOT EXISTS public.hospedes
(
       id bigserial not null,
        documento varchar(255),
        nome varchar(255),
        telefone varchar(255),
        valor_gasto numeric(38,2),
        valor_ultima_hospedagem numeric(38,2),
        primary key (id),
        unique(documento)
);   

CREATE TABLE IF NOT EXISTS public.tabela_valores
(
       id bigserial not null,
        dia_fim varchar(255),
        dia_inicio varchar(255),
        valor_diaria numeric(38,2),
        valor_vaga numeric(38,2),
        primary key (id),
        unique(dia_inicio,dia_fim)
);
       
alter table check_in 
       add constraint FKhlqql0rhurrma28t7250c9mk8 
       foreign key (hospede_id) 
       references hospedes;       