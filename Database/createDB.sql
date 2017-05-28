--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.10
-- Dumped by pg_dump version 9.3.10
-- Started on 2017-05-24 16:21:54 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE rts2_web;
--
-- TOC entry 2067 (class 1262 OID 17050)
-- Name: rts2_web; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE rts2_web WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


\connect rts2_web

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 2068 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 184 (class 3079 OID 11789)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2069 (class 0 OID 0)
-- Dependencies: 184
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 180 (class 1259 OID 17148)
-- Name: Image; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Image" (
    "Id" integer NOT NULL,
    "IdTimeTable" integer NOT NULL,
    "Description" character varying(500),
    "Source" character varying(200) NOT NULL,
    "Name" character varying(100) NOT NULL
);


--
-- TOC entry 183 (class 1259 OID 41798)
-- Name: ImageCache; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "ImageCache" (
    "Id" integer NOT NULL,
    "ImagePath" text,
    "ImageData" text,
    "ImageFitsData" text,
    "CreateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "UpdateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "IdTelescope" integer NOT NULL
);


--
-- TOC entry 182 (class 1259 OID 41796)
-- Name: ImageCache_Id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "ImageCache_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2070 (class 0 OID 0)
-- Dependencies: 182
-- Name: ImageCache_Id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "ImageCache_Id_seq" OWNED BY "ImageCache"."Id";


--
-- TOC entry 179 (class 1259 OID 17146)
-- Name: Image_Id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "Image_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2071 (class 0 OID 0)
-- Dependencies: 179
-- Name: Image_Id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "Image_Id_seq" OWNED BY "Image"."Id";


--
-- TOC entry 170 (class 1259 OID 17061)
-- Name: Role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Role" (
    "Name" character varying(10) NOT NULL,
    "CreateDate" timestamp without time zone DEFAULT now() NOT NULL
);


--
-- TOC entry 178 (class 1259 OID 17130)
-- Name: Target; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Target" (
    "Id" integer NOT NULL,
    "Name" character varying(200) NOT NULL,
    "Coordinates" bigint NOT NULL,
    "UpdateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "CreateDate" timestamp without time zone DEFAULT now() NOT NULL
);


--
-- TOC entry 177 (class 1259 OID 17128)
-- Name: Target_Id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "Target_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2072 (class 0 OID 0)
-- Dependencies: 177
-- Name: Target_Id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "Target_Id_seq" OWNED BY "Target"."Id";


--
-- TOC entry 174 (class 1259 OID 17100)
-- Name: Telescope; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Telescope" (
    "Id" integer NOT NULL,
    "Name" character varying(50) NOT NULL,
    "Address" character varying(200) NOT NULL,
    "UpdateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "CreateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "AccountName" character varying(50) NOT NULL,
    "AccountPassword" character varying(50) NOT NULL,
    "PublicTimeStart" timestamp without time zone,
    "PublicTimeEnd" timestamp without time zone,
    "IsPublicEnabled" boolean DEFAULT false NOT NULL
);


--
-- TOC entry 173 (class 1259 OID 17098)
-- Name: Telescope_Id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "Telescope_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2073 (class 0 OID 0)
-- Dependencies: 173
-- Name: Telescope_Id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "Telescope_Id_seq" OWNED BY "Telescope"."Id";


--
-- TOC entry 176 (class 1259 OID 17117)
-- Name: TimeTable; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "TimeTable" (
    "Id" integer NOT NULL,
    "IdTelescope" integer NOT NULL,
    "Start" timestamp without time zone NOT NULL,
    "End" timestamp without time zone NOT NULL,
    "IsConfirmed" bit(1),
    "IdTarget" integer NOT NULL,
    "CreateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "UpdateDate" timestamp without time zone DEFAULT now() NOT NULL
);


--
-- TOC entry 175 (class 1259 OID 17115)
-- Name: TimeTable_Id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "TimeTable_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2074 (class 0 OID 0)
-- Dependencies: 175
-- Name: TimeTable_Id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "TimeTable_Id_seq" OWNED BY "TimeTable"."Id";


--
-- TOC entry 172 (class 1259 OID 17068)
-- Name: User; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "User" (
    "Id" integer NOT NULL,
    "UserName" character varying(20) NOT NULL,
    "FirstName" character varying(50) NOT NULL,
    "LastName" character varying(50) NOT NULL,
    "Role" character varying(10) NOT NULL,
    "Email" character varying(50) NOT NULL,
    "UpdateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "CreateDate" timestamp without time zone DEFAULT now() NOT NULL,
    "Token" uuid NOT NULL,
    "TokenExpiration" timestamp without time zone DEFAULT now() NOT NULL,
    "Password" character varying(300) NOT NULL,
    "IsActive" boolean DEFAULT true NOT NULL
);


--
-- TOC entry 181 (class 1259 OID 25372)
-- Name: UserHasTelescope; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "UserHasTelescope" (
    "IdUser" integer NOT NULL,
    "IdTelescope" integer NOT NULL
);


--
-- TOC entry 171 (class 1259 OID 17066)
-- Name: User_Id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE "User_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2075 (class 0 OID 0)
-- Dependencies: 171
-- Name: User_Id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE "User_Id_seq" OWNED BY "User"."Id";


--
-- TOC entry 1917 (class 2604 OID 17151)
-- Name: Id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Image" ALTER COLUMN "Id" SET DEFAULT nextval('"Image_Id_seq"'::regclass);


--
-- TOC entry 1918 (class 2604 OID 41801)
-- Name: Id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "ImageCache" ALTER COLUMN "Id" SET DEFAULT nextval('"ImageCache_Id_seq"'::regclass);


--
-- TOC entry 1914 (class 2604 OID 17133)
-- Name: Id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Target" ALTER COLUMN "Id" SET DEFAULT nextval('"Target_Id_seq"'::regclass);


--
-- TOC entry 1907 (class 2604 OID 17103)
-- Name: Id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Telescope" ALTER COLUMN "Id" SET DEFAULT nextval('"Telescope_Id_seq"'::regclass);


--
-- TOC entry 1911 (class 2604 OID 17120)
-- Name: Id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "TimeTable" ALTER COLUMN "Id" SET DEFAULT nextval('"TimeTable_Id_seq"'::regclass);


--
-- TOC entry 1902 (class 2604 OID 17071)
-- Name: Id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "User" ALTER COLUMN "Id" SET DEFAULT nextval('"User_Id_seq"'::regclass);


--
-- TOC entry 1946 (class 2606 OID 41806)
-- Name: ImageCache_PK; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "ImageCache"
    ADD CONSTRAINT "ImageCache_PK" PRIMARY KEY ("Id");


--
-- TOC entry 1948 (class 2606 OID 41831)
-- Name: ImageCache_UQ; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "ImageCache"
    ADD CONSTRAINT "ImageCache_UQ" UNIQUE ("IdTelescope", "ImagePath");


--
-- TOC entry 1942 (class 2606 OID 17156)
-- Name: PK_Image; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Image"
    ADD CONSTRAINT "PK_Image" PRIMARY KEY ("Id");


--
-- TOC entry 1922 (class 2606 OID 17065)
-- Name: PK_Role; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Role"
    ADD CONSTRAINT "PK_Role" PRIMARY KEY ("Name");


--
-- TOC entry 1938 (class 2606 OID 17136)
-- Name: PK_Target; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Target"
    ADD CONSTRAINT "PK_Target" PRIMARY KEY ("Id");


--
-- TOC entry 1932 (class 2606 OID 17105)
-- Name: PK_Telescope; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Telescope"
    ADD CONSTRAINT "PK_Telescope" PRIMARY KEY ("Id");


--
-- TOC entry 1936 (class 2606 OID 17122)
-- Name: PK_TimeTable; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "TimeTable"
    ADD CONSTRAINT "PK_TimeTable" PRIMARY KEY ("Id");


--
-- TOC entry 1924 (class 2606 OID 17073)
-- Name: PK_User; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "User"
    ADD CONSTRAINT "PK_User" PRIMARY KEY ("Id");


--
-- TOC entry 1940 (class 2606 OID 17138)
-- Name: UQ_Target; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Target"
    ADD CONSTRAINT "UQ_Target" UNIQUE ("Name");


--
-- TOC entry 1934 (class 2606 OID 17107)
-- Name: UQ_Telescope; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Telescope"
    ADD CONSTRAINT "UQ_Telescope" UNIQUE ("Name");


--
-- TOC entry 1926 (class 2606 OID 17075)
-- Name: UQ_User; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "User"
    ADD CONSTRAINT "UQ_User" UNIQUE ("UserName");


--
-- TOC entry 1928 (class 2606 OID 17179)
-- Name: UQ_User_Email; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "User"
    ADD CONSTRAINT "UQ_User_Email" UNIQUE ("Email");


--
-- TOC entry 1930 (class 2606 OID 17177)
-- Name: UQ_User_Token; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "User"
    ADD CONSTRAINT "UQ_User_Token" UNIQUE ("Token");


--
-- TOC entry 1944 (class 2606 OID 25376)
-- Name: UserHasTelescope_PK; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "UserHasTelescope"
    ADD CONSTRAINT "UserHasTelescope_PK" PRIMARY KEY ("IdUser", "IdTelescope");


--
-- TOC entry 1952 (class 2606 OID 17157)
-- Name: FK_ImageHasTimeTable; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Image"
    ADD CONSTRAINT "FK_ImageHasTimeTable" FOREIGN KEY ("IdTimeTable") REFERENCES "TimeTable"("Id");


--
-- TOC entry 1951 (class 2606 OID 17141)
-- Name: FK_TimeTableHasTarget; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "TimeTable"
    ADD CONSTRAINT "FK_TimeTableHasTarget" FOREIGN KEY ("IdTarget") REFERENCES "Target"("Id");


--
-- TOC entry 1950 (class 2606 OID 17123)
-- Name: FK_TimeTableHasTelescope; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "TimeTable"
    ADD CONSTRAINT "FK_TimeTableHasTelescope" FOREIGN KEY ("IdTelescope") REFERENCES "Telescope"("Id");


--
-- TOC entry 1949 (class 2606 OID 17076)
-- Name: FK_UserHasRole; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "User"
    ADD CONSTRAINT "FK_UserHasRole" FOREIGN KEY ("Role") REFERENCES "Role"("Name");


--
-- TOC entry 1955 (class 2606 OID 41825)
-- Name: TelescopeHasImages_FK; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "ImageCache"
    ADD CONSTRAINT "TelescopeHasImages_FK" FOREIGN KEY ("IdTelescope") REFERENCES "Telescope"("Id");


--
-- TOC entry 1954 (class 2606 OID 25382)
-- Name: UserHasTelescope_Telescope_PK; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "UserHasTelescope"
    ADD CONSTRAINT "UserHasTelescope_Telescope_PK" FOREIGN KEY ("IdTelescope") REFERENCES "Telescope"("Id");


--
-- TOC entry 1953 (class 2606 OID 25377)
-- Name: UserHasTelescope_User_FK; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "UserHasTelescope"
    ADD CONSTRAINT "UserHasTelescope_User_FK" FOREIGN KEY ("IdUser") REFERENCES "User"("Id");


-- Completed on 2017-05-24 16:21:54 CEST

--
-- PostgreSQL database dump complete
--
