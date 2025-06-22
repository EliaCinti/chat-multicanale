package com.chatmulticanale.view.costanti_view;

public class CostantiView {

    private CostantiView() {
        // costruttore vuoto
    }

    // --- COSTANTI COMUNI ---
        public static final String SELEZIONA_OPZIONE = "Seleziona un'opzione: ";
        public static final String NO_PROGETTI = "Non hai progetti da gestire.";
        public static final String OPERAZIONE_ANNULLATA = "\nOperazione annullata.";
        public static final String COMANDO_NON_RICONOSCIUTO = "ERRORE: Comando non riconosciuto.";
        public static final String NO_ALTRE_PAGINE = "Non ci sono altre pagine.";
        public static final String PRIMA_PAGINA = "Sei già alla prima pagina.";
        public static final String SELEZIONA_PROGETTO = "Seleziona il progetto di cui vuoi modificare un canale:";
        public static final String NO_CANALI = "\nAttenzione: Questo progetto non ha canali.";
        public static final String ID_DIPENDENTE = "ID Dipendente: ";
        public static final String WELCOME = "Benvenuto/a, ";

        // --- formati ---
        public static final String FORMATO_DATA = "dd-MM-yyyy HH:mm:ss";
        public static final String FORMATO_ID_NOME = "  ID: %-5d | Nome: %s";
        public static final String FORMATO_ID_NOME_COGNOME = "  ID: %-5d | Nome: %s %s";
        public static final String FORMATO_NOME_UTENTE = "ID: %-5d | Nome: %-15s";
        public static final String FORMATO_NOME_COMPLETO_UTENTE = "ID: %-5d | Nome: %-15s | Cognome: %s";
        public static final String FORMATO_ID_NOME_NOME_COGNOME = "ID: %-5d | Nome: %-25s | Responsabile: %s %s";

        // --- premi invio per ---
        public static final String INVIO_PER_HOME = "Premi Invio per tornare alla home...";
        public static final String INVIO_PER_SELEZIONE_PROGETTO = "Premi Invio per tornare alla selezione del progetto...";
        public static final String INVIO_PER_SELEZIONE_CANALE = "Premi Invio per tornare alla selezione del canale...";
        public static final String INVIO_PER_MENU = "Premi Invio per tornare al menu...";
        public static final String A_CAPO_INVIO_PER_MENU = "\nPremi Invio per tornare al menu...";
        public static final String INVIO_PER_RIPROVARE = "Premi Invio per riprovare...";
        public static final String INVIO_PER_CONTINUARE = "Premi Invio per continuare...";
        public static final String INVIO_PER_INDIETRO = "Premi Invio per tornare indietro...";

        // --- /b o /back ---
        public static final String B_O_BACK = "Digita '/b' o '/back' per annullare in qualsiasi momento.";
        public static final String B_O_BACK_2 = "Digita '/b' o '/back' per tornare indietro in qualsiasi momento.";


    public static final String CITAZIONE_ANNULLATA = "Citazione annullata.";
    public static final String MESSAGGIO_INVIATO = "Messaggio inviato!";
    public static final String ERRORE_INVIO_MESSAGGIO = "ERRORE: impossibile inviare il messaggio.";
    public static final String NO_MESSAGGIO_IN_CONVERSAZIONE = "Nessun messaggio in questa conversazione.";
    public static final String NO_MESSAGGIO_IN_CANALE = "Nessun messaggio in questo canale.";
    public static final String MODALITA_LETTURA = " (Modalità Sola Lettura)";
    public static final String NO_MESSAGGI_IN_PAGINA_DA_CITARE = "Non ci sono messaggi in questa pagina da poter citare.";
    public static final String ID_NON_VALIDO_PER_CITAZIONE = "ID non valido. Puoi citare solo i messaggi presenti in questa pagina.";
    public static final String ID_NON_VALIDO_PER_AVVIARE_CHAT = "ID non valido. Puoi avviare una chat solo da un messaggio presente in questa pagina.";
    public static final String NO_MESSAGGI_DA_CUI_PARTIRE = "ATTENZIONE: Non ci sono messaggi in questa pagina da cui partire.";
    public static final String CHAT_AVVIATA = "\nChat privata avviata con successo!";
    public static final String ERRORE_AVVIO_CHAT = "\nERRORE: Impossibile avviare la chat.";
    public static final String NO_CHAT_ATTIVE = "Non hai nessuna chat privata attiva.";
    public static final String ID_NON_VALIDO_IN_PAGINA_CORRENTE = "ID non valido. Seleziona un ID dalla pagina corrente.";
    public static final String ELENCO_CHAT_PRIVATE = "Elenco delle tue conversazioni private:";

    // --- COSTANTI LOGIN ---
    public static final String LOGIN = "--- LOGIN ---";
    public static final String USERNAME = "Username: ";
    public static final String PASSWORD = "Password: ";
    public static final String LOGIN_SI = "\nLogin effettuato con successo!";
    public static final String LOGIN_NO = "\nCredenziali non valide. Riprova.";

    // --- COSTANTI AMMINISTRATORE ---

        // --- opzioni home amministratore ---
        public static final String HOME_AMMINISTRATORE = "--- HOME AMMINISTRATORE ---";
        public static final String HOME_AMMINISTRATORE_OPZIONE_1 = "1. Promuovi un utente a Capo Progetto";
        public static final String HOME_AMMINISTRATORE_OPZIONE_2 = "2. Rimuovi ruolo Capo Progetto";
        public static final String HOME_AMMINISTRATORE_OPZIONE_3 = "3. Assegna responsabilità Progetto a un Capo Progetto";
        public static final String HOME_AMMINISTRATORE_OPZIONE_4 = "4. Riassegna responsabilità Progetto";
        public static final String HOME_AMMINISTRATORE_OPZIONE_5 = "5. Aggiungi progetto";
        public static final String HOME_AMMINISTRATORE_OPZIONE_0 = "0. Logout";

        // --- costanti promuovi utente ---
        public static final String PROMUOVI_UTENTE = "--- PROMUOVI UTENTE A CAPO PROGETTO ---";
        public static final String PROMUOVI_UTENTE_NO_DIPENDENTI = "Nessun dipendente da promuovere al momento.";
        public static final String PROMUOVI_UTENTE_LISTA_DIPENDENTI = "Lista dei Dipendenti che possono essere promossi:";
        public static final String PROMUOVI_UTENTE_ID_UTENTE = "Inserisci l'ID dell'utente da promuovere: ";
        public static final String PROMUOVI_UTENTE_SUCCESSO = "\nUtente promosso con successo!";
        public static final String PROMUOVI_UTENTE_ERRORE = "\nERRORE: Impossibile promuovere l'utente. Controllare i log.";

        // --- costanti assegnazione ---
        public static final String ASSEGNA_RESPONSABILITA = "--- ASSEGNA RESPONSABILITÀ PROGETTO ---";
        public static final String ASSEGNAZIONE_NESSUN_PROGETTO_DA_ASSEGNARE = "Nessun progetto da assegnare al momento.";public static final String ASSEGNAZIONE_NO_CAPI_PROGETTO_DISPONIBILI = "Nessun Capo Progetto disponibile a cui assegnare progetti.";
        public static final String ASSEGNAZIONE_LISTA_PROGETTI_NON_ASSEGNATI = "Lista dei Progetti non assegnati:";
        public static final String ASSEGNAZIONE_LISTA_CAPI_PROGETTO = "\nLista dei Capi Progetto disponibili:";
        public static final String ASSEGNAZIONE_PROGETTO_ASSEGNATO = "\nProgetto assegnato con successo!";
        public static final String ASSEGNAZIONE_PROGETTO_NON_ASSEGNATO = "\nERRORE: Impossibile assegnare il progetto. Controllare i log.";
        public static final String ASSEGNAZIONE_INSERIRE_ID_CAPO_PROGETTO = "Inserisci l'ID del Capo Progetto a cui assegnare il progetto: ";
        public static final String ASSEGNAZIONE_INSERIRE_ID_PROGETTO = "Inserisci l'ID del progetto da assegnare: ";

        // --- costanti riassegnazione ---
        public static final String RIASSEGNA_RESPONSABILITA = "--- RIASSEGNA RESPONSABILITÀ PROGETTO ---";
        public static final String RIASSEGNAZIONE_OK = "\nProgetto riassegnato con successo!";
        public static final String RIASSEGNAZIONE_ID_NUOVO_CAPO_PROGETTO = "Inserisci l'ID del NUOVO Capo Progetto responsabile: ";
        public static final String RIASSEGNAZIONE_LISTA_CAPI_PROGETTO_DISPONIBILI = "\nLista dei Capi Progetto disponibili (escluso il responsabile attuale):";
        public static final String RIASSEGNAZIONE_NESSUN_CAPO_PROGETTO_DISPONIBILE = "\nERRORE: Non ci sono altri Capi Progetto disponibili a cui riassegnare questo progetto.";
        public static final String RIASSEGNAZIONE_INSERISCI_ID_PROGETTO = "Inserisci l'ID del progetto da riassegnare: ";
        public static final String RIASSEGNAZIONE_LISTA_PROGETTI = "Lista dei Progetti attualmente assegnati:";
        public static final String RIASSEGNAZIONE_NO_ABBASTANZA_CAPI_PROGETTO = "ATTENZIONE: Non è possibile effettuare riassegnazioni perché non ci sono abbastanza Capi Progetto.";
        public static final String RIASSEGNAZIONE_NO_PROGETTI_ASSEGNATI = "Nessun progetto attualmente assegnato da poter riassegnare.";
        public static final String RIASSEGNAZIONE_SUCCESSO = "Progetto riassegnato con successo.";
        public static final String RIASSEGNAZIONE_ERRORE = "\nERRORE: Impossibile procedere. Questo utente ha progetti da riassegnare, ma non ci sono altri Capi Progetto disponibili.";
        public static final String RIASSEGNAZIONE_ERRORE_2 = "ERRORE durante la riassegnazione. Procedura annullata.";
        public static final String RIASSEGNAZIONE_ERRORE_3 = "\nERRORE: Impossibile riassegnare il progetto. Controllare i log.";
        public static final String RIASSEGNAZIONE_ERRORE_4 = "Logica inconsistente: progetto non trovato dopo validazione.";

        // --- costanti degrada ruolo ---
        public static final String RIMUOVI_RUOLO_CAPO_PROGETTO = "--- RIMUOVI RUOLO CAPO PROGETTO ---";
        public static final String DEGRADA_RUOLO_ID_CAPO_PROGETTO = "Inserisci l'ID del Capo Progetto da degradare: ";
        public static final String DEGRADA_RUOLO_LISTA_CAPI_PROGETTO = "Lista dei Capi Progetto attuali:";
        public static final String DEGRADA_RUOLO_NO_CAPO_PROGETTO_DA_RIMUOVERE = "Nessun Capo Progetto da rimuovere.";
        public static final String DEGRADA_RUOLO_WARNING = "\nATTENZIONE: Questo utente è responsabile dei seguenti progetti.";
        public static final String DEGRADA_RUOLO_RIASSEGNAZIONE_NECESSARIA = "È necessario riassegnarli tutti prima di poter procedere con il degrado.";
        public static final String DEGRADA_RUOLO_CAPI_PROGETTO_DISPONIBILI = "\nCapi Progetto disponibili per la riassegnazione:";
        public static final String DEGRADA_RUOLO_INSERISCI_ID_NUOVO_CAPO_PROGETTO = "\nInserisci l'ID del nuovo responsabile per questo progetto: ";
        public static final String DEGRADA_RUOLO_NO_RESPONSABILITA = "\nL'utente selezionato non ha responsabilità di progetto attive.";
        public static final String DEGRADA_RUOLO_ERRORE = "ERRORE finale durante il degrado del ruolo.";
        public static final String DEGRADA_RUOLO_SUCCESSO = "Ruolo rimosso con successo! L'utente è ora un Dipendente.";
        public static final String DEGRADA_RUOLO_SUCCESSO_2 = "\nTutte le responsabilità sono state trasferite con successo.";

        // --- costanti aggiungi progetto ---
        public static final String AGGIUNGI_PROGETTO = "--- AGGIUNGI PROGETTO ---";
        public static final String AGGIUNGI_PROGETTO_IMPOSSIBILE_CREARE_PROGETTO = "\nERRORE: Impossibile creare il progetto. Il nome potrebbe essere già esistente.";
        public static final String AGGIUNGI_PROGETTO_NOME_PROGETTO = "Nome del nuovo progetto: ";
        public static final String AGGIUNGI_PROGETTO_DESCRIZIONE_PROGETTO = "Descrizione del progetto: ";

    // --- COSTANTI CAPO PROGETTO ---

        // --- costanti opzioni home capo progetto ---
        public static final String HOME_CAPO_PROGETTO = "--- HOME CAPO PROGETTO ---";
        public static final String HOME_CAPO_PROGETTO_OPZIONI_GESTIONE_PROGETTI = "Opzioni di Gestione Progetti:";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_1 = "1. Visualizza i progetti di cui sei responsabile";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_2 = "2. Crea un nuovo canale per un progetto";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_3 = "3. Aggiungi un dipendente a un canale";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_4 = "4. Rimuovi un dipendente da un canale";
        public static final String HOME_CAPO_PROGETTO_OPZIONI_INTERAZIONE_UTENTE = "Opzioni di Interazione Utente:";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_5 = "5. Supervisiona le chat private di un progetto";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_6 = "6. Accedi ai tuoi canali e chat";
        public static final String HOME_CAPO_PROGETTO_OPZIONE_0 = "0. Logout";

        // --- costanti visualizza i miei progetti ---
        public static final String I_MIEI_PROGETTI = "--- I MIEI PROGETTI ---";
        public static final String VISUALIZZA_PROGETTI_RESPONSABILE_DI_NESSUN_PROGETTO = "Al momento non sei responsabile di nessun progetto.";
        public static final String VISUALIZZA_PROGETTI_ELENCO_PROGETTI = "Elenco dei progetti di cui sei responsabile:";

        // --- costanti crea canale ---
        public static final String CREA_NUOVO_CANALE = "--- CREA NUOVO CANALE ---";
        public static final String CREA_CANALE_NO_PROGETTI = "Non hai progetti di cui sei responsabile, quindi non puoi creare canali.";
        public static final String CREA_CANALE_SELEZIONA_PROGETTO = "Seleziona il progetto per cui vuoi creare un nuovo canale:";
        public static final String CREA_CANALE_DETTAGLI = "\nOra inserisci i dettagli per il nuovo canale.";
        public static final String CREA_CANALE_NOME_CANALE = "Nome del canale: ";
        public static final String CREA_CANALE_DESCRIZIONE_CANALE = "Descrizione del canale (opzionale, premi Invio per saltare): ";
        public static final String CREA_CANALE_SUCCESSO_PARTE_1 = "\nCanale '";
        public static final String CREA_CANALE_SUCCESSO_PARTE_2 = "' creato con successo!";
        public static final String CREA_CANALE_ERRORE = "\nERRORE: Impossibile creare il canale. Il nome potrebbe essere già in uso per questo progetto.";

        // --- costanti aggiungi utente ---
        public static final String AGGIUNGI_DIPENDENTE_A_CANALE = "--- AGGIUNGI DIPENDENTE A CANALE ---";
        public static final String AGGIUNGI_DIPENDENTE_SELEZIONE_CANALE = "Seleziona il canale a cui aggiungere un dipendente (o usa /b per scegliere un altro progetto):";
        public static final String AGGIUNGI_DIPENDENTE_NO_ALTRI_DIPENDENTI = "\nAttenzione: Non ci sono altri dipendenti da poter aggiungere a questo canale.";
        public static final String AGGIUNGI_DIPENDENTE_SELEZIONA_DIPENDENTE = "\nSeleziona il dipendente da aggiungere al canale:";
        public static final String AGGIUNGI_DIPENDENTE_SUCCESSO = "\nDipendente aggiunto al canale con successo!";
        public static final String AGGIUNGI_DIPENDENTE_ERRORE = "\nERRORE: Impossibile aggiungere il dipendente.";

        // --- costanti rimuovi utente ---
        public static final String RIMUOVI_DIPENDENTE_DA_CANALE = "--- RIMUOVI DIPENDENTE DA CANALE ---";
        public static final String RIMUOVI_DIPENDENTE_SELEZIONE_CANALE = "Seleziona il canale da cui rimuovere un dipendente (o usa /b per scegliere un altro progetto):";
        public static final String RIMUOVI_DIPENDENTE_NO_DIPENDENTI_WARNING = "\nATTENZIONE: Non ci sono dipendenti da poter rimuovere da questo canale.";
        public static final String RIMUOVI_DIPENDENTE_SELEZIONA_DIPENDENTE = "\nSeleziona il dipendente da rimuovere dal canale:";
        public static final String RIMUOVI_DIPENDENTE_SUCCESSO = "\nDipendente rimosso dal canale con successo!";
        public static final String RIMUOVI_DIPENDENTE_ERRORE = "\nERRORE: Impossibile rimuovere il dipendente.";

        // --- costanti supervisione chat private ---
        public static final String SUPERVISIONE_CHAT_PRIVATE = "--- SUPERVISIONE CHAT PRIVATE ---";
        public static final String SUPERVISIONE_CHAT_SELEZIONA_PROGETTO = "Seleziona il progetto di cui vuoi supervisionare le chat private:";
        public static final String SUPERVISIONE_CHAT_ERRORE = "ERRORE: Non sei autorizzato o si è verificato un problema.";
        public static final String SUPERVISIONE_CHAT_NO_CHAT_WARNING = "\nATTENZIONE: Non ci sono chat private originate da questo progetto da supervisionare.";
        public static final String SUPERVISIONE_CHAT_ELENCO_CHAT = "\nElenco delle chat private originate dal progetto '";
        public static final String SUPERVISIONE_CHAT_INSERISCI_ID_CHAT = "Inserisci l'ID della chat da visualizzare (o usa /b per tornare alla selezione del progetto):";

    // --- COSTANTI DIPENDENTE ---

        // --- costanti home dipendente ---
        public static final String HOME_DIPENDENTE = "--- HOME DIPENDENTE ---";
        public static final String HOME_DIPENDENTE_OPZIONE_1 = "1. Accedi ai tuoi canali e chat";
        public static final String HOME_DIPENDENTE_OPZIONE_0 = "0. Logout";

    // --- COSTANTI COMUNI TRA CAPO PROGETTO E DIPENDENTE ---
    public static final String AREA_COMUNICAZIONI = "--- AREA COMUNICAZIONI ---";

        // --- costanti accedi a canali e chat ---
        public static final String ACCEDI_A_CANALI_E_CHAT_OPZIONE_1 = "1. Accedi ai Canali di Progetto";
        public static final String ACCEDI_A_CANALI_E_CHAT_OPZIONE_2 = "2. Accedi alle Chat Private";

        // --- costanti accesso a canali progetto ---
        public static final String ACCESSO_AI_CANALI_DI_PROGETTO = "--- I TUOI CANALI DI PROGETTO ---";
        public static final String SELEZIONA_CANALE_PER_MESSAGGI = "Seleziona un canale per visualizzarne i messaggi:";
        public static final String NO_PARTECIPAZIONE_A_CANALI = "Non partecipi a nessun canale di progetto.";

        // --- costanti accesso a chat private ---
        public static final String AREA_CHAT_PRIVATE = "--- AREA CHAT PRIVATE ---";
        public static final String ACCEDI_CHAT_PRIVATE_OPZIONE_1 = "1. Visualizza le tue chat";
        public static final String ACCEDI_CHAT_PRIVATE_OPZIONE_2 = "2. Avvia una nuova chat da un messaggio di un canale";

        // --- costanti visualizza chat private ---
        public static final String LE_TUE_CHAT_PRIVATE = "--- LE TUE CHAT PRIVATE ---";
        public static final String VISUALIZZA_CHAT_NO_CHAT = "Non hai nessuna chat privata attiva.";
        public static final String VISUALIZZA_CHAT_ELENCO_CHAT = "Elenco delle tue conversazioni private:";
        public static final String VISUALIZZA_CHAT_INSERISCI_ID_CHAT = "Inserisci l'ID della chat da visualizzare: ";

        // --- costanti avvia nuova chat privata ---
        public static final String AVVIA_NUOVA_CHAT_PRIVATA = "--- AVVIA NUOVA CHAT PRIVATA ---";
        public static final String AVVIA_NUOVA_CHAT_NO_CANALI = "Devi partecipare ad almeno un canale per poter avviare una chat.";
        public static final String AVVIA_NUOVA_CHAT_SELEZIONA_CANALE = "Seleziona il canale contenente il messaggio da cui partire:";
}
