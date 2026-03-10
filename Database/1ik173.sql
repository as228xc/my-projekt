--
-- PostgreSQL database dump
--

\restrict I2DA3SkvcIYYguBDbndyi8QA8Il8jkaKCNHPcp96HVrs6mfQOde0PHGnShRwk7c

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-03-10 11:58:19

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16430)
-- Name: book_copies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_copies (
    copy_id integer NOT NULL,
    isbn integer NOT NULL,
    available boolean DEFAULT true NOT NULL
);


ALTER TABLE public.book_copies OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16429)
-- Name: book_copies_copy_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_copies_copy_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.book_copies_copy_id_seq OWNER TO postgres;

--
-- TOC entry 5060 (class 0 OID 0)
-- Dependencies: 221
-- Name: book_copies_copy_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_copies_copy_id_seq OWNED BY public.book_copies.copy_id;


--
-- TOC entry 220 (class 1259 OID 16419)
-- Name: book_titles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_titles (
    isbn integer NOT NULL,
    title character varying(255) NOT NULL,
    author character varying(255) NOT NULL
);


ALTER TABLE public.book_titles OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16447)
-- Name: loans; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loans (
    loan_id integer NOT NULL,
    member_id integer NOT NULL,
    copy_id integer NOT NULL,
    loan_date date NOT NULL,
    due_date date NOT NULL,
    return_date date
);


ALTER TABLE public.loans OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16446)
-- Name: loans_loan_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.loans_loan_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.loans_loan_id_seq OWNER TO postgres;

--
-- TOC entry 5063 (class 0 OID 0)
-- Dependencies: 223
-- Name: loans_loan_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.loans_loan_id_seq OWNED BY public.loans.loan_id;


--
-- TOC entry 219 (class 1259 OID 16399)
-- Name: members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.members (
    member_id integer NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    personal_number character varying(20) NOT NULL,
    member_type character varying(20) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    borrowed_count integer DEFAULT 0 NOT NULL,
    late_returns_count integer DEFAULT 0 NOT NULL,
    suspensions_count integer DEFAULT 0 NOT NULL,
    suspended_until date,
    blacklisted boolean DEFAULT false NOT NULL
);


ALTER TABLE public.members OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16469)
-- Name: violations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.violations (
    violation_id integer NOT NULL,
    member_id integer NOT NULL,
    violation_date date NOT NULL,
    violation_type character varying(50) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.violations OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16468)
-- Name: violations_violation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.violations_violation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.violations_violation_id_seq OWNER TO postgres;

--
-- TOC entry 5066 (class 0 OID 0)
-- Dependencies: 225
-- Name: violations_violation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.violations_violation_id_seq OWNED BY public.violations.violation_id;


--
-- TOC entry 4879 (class 2604 OID 16433)
-- Name: book_copies copy_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_copies ALTER COLUMN copy_id SET DEFAULT nextval('public.book_copies_copy_id_seq'::regclass);


--
-- TOC entry 4881 (class 2604 OID 16450)
-- Name: loans loan_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loans ALTER COLUMN loan_id SET DEFAULT nextval('public.loans_loan_id_seq'::regclass);


--
-- TOC entry 4882 (class 2604 OID 16472)
-- Name: violations violation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.violations ALTER COLUMN violation_id SET DEFAULT nextval('public.violations_violation_id_seq'::regclass);


--
-- TOC entry 5049 (class 0 OID 16430)
-- Dependencies: 222
-- Data for Name: book_copies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_copies (copy_id, isbn, available) FROM stdin;
\.


--
-- TOC entry 5047 (class 0 OID 16419)
-- Dependencies: 220
-- Data for Name: book_titles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_titles (isbn, title, author) FROM stdin;
\.


--
-- TOC entry 5051 (class 0 OID 16447)
-- Dependencies: 224
-- Data for Name: loans; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loans (loan_id, member_id, copy_id, loan_date, due_date, return_date) FROM stdin;
\.


--
-- TOC entry 5046 (class 0 OID 16399)
-- Dependencies: 219
-- Data for Name: members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.members (member_id, first_name, last_name, personal_number, member_type, active, borrowed_count, late_returns_count, suspensions_count, suspended_until, blacklisted) FROM stdin;
1	Alma	Svensson	200306098922	UNDERGRADUATE	f	0	0	0	\N	t
\.


--
-- TOC entry 5053 (class 0 OID 16469)
-- Dependencies: 226
-- Data for Name: violations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.violations (violation_id, member_id, violation_date, violation_type, description) FROM stdin;
\.


--
-- TOC entry 5067 (class 0 OID 0)
-- Dependencies: 221
-- Name: book_copies_copy_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_copies_copy_id_seq', 1, false);


--
-- TOC entry 5068 (class 0 OID 0)
-- Dependencies: 223
-- Name: loans_loan_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.loans_loan_id_seq', 1, false);


--
-- TOC entry 5069 (class 0 OID 0)
-- Dependencies: 225
-- Name: violations_violation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.violations_violation_id_seq', 1, false);


--
-- TOC entry 4890 (class 2606 OID 16439)
-- Name: book_copies book_copies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_copies
    ADD CONSTRAINT book_copies_pkey PRIMARY KEY (copy_id);


--
-- TOC entry 4888 (class 2606 OID 16428)
-- Name: book_titles book_titles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_titles
    ADD CONSTRAINT book_titles_pkey PRIMARY KEY (isbn);


--
-- TOC entry 4892 (class 2606 OID 16457)
-- Name: loans loans_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loans
    ADD CONSTRAINT loans_pkey PRIMARY KEY (loan_id);


--
-- TOC entry 4884 (class 2606 OID 16418)
-- Name: members members_personal_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT members_personal_number_key UNIQUE (personal_number);


--
-- TOC entry 4886 (class 2606 OID 16416)
-- Name: members members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT members_pkey PRIMARY KEY (member_id);


--
-- TOC entry 4894 (class 2606 OID 16478)
-- Name: violations violations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.violations
    ADD CONSTRAINT violations_pkey PRIMARY KEY (violation_id);


--
-- TOC entry 4895 (class 2606 OID 16440)
-- Name: book_copies book_copies_isbn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_copies
    ADD CONSTRAINT book_copies_isbn_fkey FOREIGN KEY (isbn) REFERENCES public.book_titles(isbn);


--
-- TOC entry 4896 (class 2606 OID 16463)
-- Name: loans loans_copy_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loans
    ADD CONSTRAINT loans_copy_id_fkey FOREIGN KEY (copy_id) REFERENCES public.book_copies(copy_id);


--
-- TOC entry 4897 (class 2606 OID 16458)
-- Name: loans loans_member_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loans
    ADD CONSTRAINT loans_member_id_fkey FOREIGN KEY (member_id) REFERENCES public.members(member_id);


--
-- TOC entry 4898 (class 2606 OID 16479)
-- Name: violations violations_member_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.violations
    ADD CONSTRAINT violations_member_id_fkey FOREIGN KEY (member_id) REFERENCES public.members(member_id);


--
-- TOC entry 5059 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE book_copies; Type: ACL; Schema: public; Owner: postgres
--

GRANT INSERT,DELETE,UPDATE ON TABLE public.book_copies TO elintonning;


--
-- TOC entry 5061 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE book_titles; Type: ACL; Schema: public; Owner: postgres
--

GRANT INSERT,DELETE,UPDATE ON TABLE public.book_titles TO elintonning;


--
-- TOC entry 5062 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE loans; Type: ACL; Schema: public; Owner: postgres
--

GRANT INSERT,DELETE,UPDATE ON TABLE public.loans TO elintonning;


--
-- TOC entry 5064 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE members; Type: ACL; Schema: public; Owner: postgres
--

GRANT INSERT,DELETE,UPDATE ON TABLE public.members TO elintonning;


--
-- TOC entry 5065 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE violations; Type: ACL; Schema: public; Owner: postgres
--

GRANT INSERT,DELETE,UPDATE ON TABLE public.violations TO elintonning;


-- Completed on 2026-03-10 11:58:19

--
-- PostgreSQL database dump complete
--

\unrestrict I2DA3SkvcIYYguBDbndyi8QA8Il8jkaKCNHPcp96HVrs6mfQOde0PHGnShRwk7c

