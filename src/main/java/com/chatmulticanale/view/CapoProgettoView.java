package com.chatmulticanale.view;

import com.chatmulticanale.controller.GestioneProgettiController;
import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.ChatSupervisioneDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;
import java.util.List;

public class CapoProgettoView implements View {

    private final GestioneProgettiController gestioneController;
    private final InterazioneUtenteController interazioneController;

    public CapoProgettoView(GestioneProgettiController gestioneController, InterazioneUtenteController interazioneController) {
        this.gestioneController = gestioneController;
        this.interazioneController = interazioneController;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- HOME CAPO PROGETTO ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println(ColorUtils.ANSI_BOLD + "Opzioni di Gestione Progetti:" + ColorUtils.ANSI_RESET);
            ViewUtils.println("1. Visualizza i progetti di cui sei responsabile");
            ViewUtils.println("2. Crea un nuovo canale per un progetto (CP1)");
            ViewUtils.println("3. Aggiungi un dipendente a un canale (CP2)");
            ViewUtils.println("4. Rimuovi un dipendente da un canale (CP3)");
            ViewUtils.println("5. Supervisiona le chat private di un progetto (CP4)");
            ViewUtils.printSeparator();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "Opzioni di Interazione Utente:" + ColorUtils.ANSI_RESET);
            ViewUtils.println("6. Accedi ai tuoi canali e chat");
            ViewUtils.printSeparator();
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");

            switch (scelta) {
                case 1:
                    handleVisualizzaMieiProgetti();
                    break;
                case 2:
                    handleCreaCanale();
                    break;
                case 3:
                    handleAggiungiUtenteACanale();
                    break;
                case 4:
                    handleRimuoviUtenteDaCanale();
                    break;
                case 5:
                    handleSupervisionaChatPrivate();
                    break;
                case 6:
                    handleAccediACanaliEChat();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
                default:
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nScelta non valida." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per riprovare...");
            }
        }
    }

    private void handleVisualizzaMieiProgetti() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- I MIEI PROGETTI ---" + ColorUtils.ANSI_RESET);
        ViewUtils.printSeparator();
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
        if (mieiProgetti.isEmpty()) {
            ViewUtils.println("Al momento non sei responsabile di nessun progetto.");
        } else {
            ViewUtils.println("Elenco dei progetti di cui sei responsabile:");
            mieiProgetti.forEach(p -> {
                String riga = String.format("  ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto());
                ViewUtils.println(riga);
            });
        }
        ViewUtils.printSeparator();
        InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
    }

    private void handleCreaCanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- CREA NUOVO CANALE (CP1) ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Digita '/b' o '/back' per annullare in qualsiasi momento.");
            ViewUtils.printSeparator();
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti di cui sei responsabile, quindi non puoi creare canali.");
                InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
                return;
            }
            int idProgettoSelezionato = selezionaProgetto(mieiProgetti, "Seleziona il progetto per cui vuoi creare un nuovo canale:");
            ViewUtils.println("\nOra inserisci i dettagli per il nuovo canale.");
            String nomeCanale = InputUtils.askForInput("Nome del canale: ");
            String descrizioneCanale = InputUtils.askForInput("Descrizione del canale: ");
            if (gestioneController.creaNuovoCanalePerProgetto(nomeCanale, descrizioneCanale, idProgettoSelezionato, idUtenteLoggato)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nCanale '" + nomeCanale + "' creato con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile creare il canale. Il nome potrebbe essere già in uso per questo progetto." + ColorUtils.ANSI_RESET);
            }
        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }
        InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
    }

    private void handleAggiungiUtenteACanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return;
            }
            progettoLoop:
            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AGGIUNGI DIPENDENTE A CANALE (CP2) ---" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
                ViewUtils.printSeparator();
                int idProgetto = selezionaProgetto(mieiProgetti, "Seleziona il progetto di cui vuoi modificare un canale:");
                List<CanaleProgetto> canali = gestioneController.getCanaliDelProgetto(idProgetto);
                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Questo progetto non ha canali." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del progetto...");
                    continue;
                }
                while (true) {
                    try {
                        int idCanale = selezionaCanale(canali, "Seleziona il canale a cui aggiungere un dipendente (o usa /b per scegliere un altro progetto):");
                        List<Utente> dipendenti = gestioneController.getDipendentiAggiungibili(idCanale);
                        if (dipendenti.isEmpty()) {
                            ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Non ci sono altri dipendenti da poter aggiungere a questo canale." + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del canale...");
                            ViewUtils.clearScreen();
                            continue;
                        }
                        ViewUtils.println("\nSeleziona il dipendente da aggiungere al canale:");
                        dipendenti.forEach(d -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s %s", d.getIdUtente(), d.getNome(), d.getCognome())));
                        ViewUtils.printSeparator();
                        int idDipendente = InputHelper.chiediIdValido("ID Dipendente: ", dipendenti);
                        if (gestioneController.aggiungiDipendenteACanale(idCanale, idDipendente)) {
                            ViewUtils.println(ColorUtils.ANSI_GREEN + "\nDipendente aggiunto al canale con successo!" + ColorUtils.ANSI_RESET);
                        } else {
                            ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile aggiungere il dipendente." + ColorUtils.ANSI_RESET);
                        }
                        break progettoLoop;
                    } catch (CommandException e) {
                        break;
                    }
                }
            }
        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata. Ritorno alla home...");
        }
        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla home...");
    }

    private void handleRimuoviUtenteDaCanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return;
            }
            progettoLoop:
            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + "--- RIMUOVI DIPENDENTE DA CANALE (CP3) ---" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
                ViewUtils.printSeparator();
                int idProgetto = selezionaProgetto(mieiProgetti, "Seleziona il progetto da cui vuoi modificare un canale:");
                List<CanaleProgetto> canali = gestioneController.getCanaliDelProgetto(idProgetto);
                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Questo progetto non ha canali." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del progetto...");
                    continue;
                }
                while (true) {
                    try {
                        int idCanale = selezionaCanale(canali, "Seleziona il canale da cui rimuovere un dipendente (o usa /b per scegliere un altro progetto):");
                        List<Utente> dipendentiNelCanale = gestioneController.getDipendentiDelCanale(idCanale);
                        if (dipendentiNelCanale.isEmpty()) {
                            ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Non ci sono dipendenti da poter rimuovere da questo canale." + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del canale...");
                            ViewUtils.clearScreen();
                            continue;
                        }
                        ViewUtils.println("\nSeleziona il dipendente da rimuovere dal canale:");
                        dipendentiNelCanale.forEach(d -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s %s", d.getIdUtente(), d.getNome(), d.getCognome())));
                        ViewUtils.printSeparator();
                        int idDipendente = InputHelper.chiediIdValido("ID Dipendente: ", dipendentiNelCanale);
                        if (gestioneController.rimuoviDipendenteDaCanale(idCanale, idDipendente)) {
                            ViewUtils.println(ColorUtils.ANSI_GREEN + "\nDipendente rimosso dal canale con successo!" + ColorUtils.ANSI_RESET);
                        } else {
                            ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile rimuovere il dipendente." + ColorUtils.ANSI_RESET);
                        }
                        break progettoLoop;
                    } catch (CommandException e) {
                        break;
                    }
                }
            }
        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata. Ritorno alla home...");
        }
        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla home...");
    }

    private void handleSupervisionaChatPrivate() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- SUPERVISIONE CHAT PRIVATE (CP4) ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
            ViewUtils.printSeparator();
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return;
            }
            int idProgetto = selezionaProgetto(mieiProgetti, "Seleziona il progetto di cui vuoi supervisionare le chat private:");
            List<ChatSupervisioneDTO> chatDaSupervisionare = gestioneController.getChatPrivateDaSupervisionare(idProgetto, idUtenteLoggato);
            if (chatDaSupervisionare == null) {
                ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE: Non sei autorizzato o si è verificato un problema." + ColorUtils.ANSI_RESET);
            } else if (chatDaSupervisionare.isEmpty()) {
                ViewUtils.println("Non ci sono chat private originate da questo progetto da supervisionare.");
            } else {
                String nomeProgettoSelezionato = mieiProgetti.stream()
                        .filter(p -> p.getIdProgetto() == idProgetto).findFirst()
                        .map(Progetto::getNomeProgetto).orElse("");
                ViewUtils.println("\nElenco delle chat private originate dal progetto '" + nomeProgettoSelezionato + "':");
                ViewUtils.printSeparator();
                String header = String.format("%-5s | %-20s | %-25s | %-18s | %s", "ID", "Data Creazione", "Partecipanti", "Canale Origine", "Messaggio Origine");
                ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);
                for (ChatSupervisioneDTO chat : chatDaSupervisionare) {
                    String partecipanti = chat.getCreatoreChatUsername() + " <-> " + chat.getPartecipanteChatUsername();
                    String riga = String.format("%-5d | %-20s | %-25s | %-18s | '%s'",
                            chat.getIdChat(), chat.getDataCreazioneChat(), partecipanti,
                            chat.getCanaleOrigineNome(), chat.getMessaggioOrigineContenuto());
                    ViewUtils.println(riga);
                }
                ViewUtils.printSeparator();
                try {
                    String promptChat = "Inserisci l'ID della chat da visualizzare (o usa /b per tornare):";
                    int idChatSelezionata = InputHelper.chiediIdValido(promptChat, chatDaSupervisionare);
                    new ChatView(interazioneController, idChatSelezionata, TipoContestoChat.CHAT_PRIVATA, true, idUtenteLoggato).show();
                } catch (CommandException e) {
                    // Non fa nulla, prosegue
                }
            }
        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata. Ritorno alla home...");
        }
        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla home...");
    }

    private void handleAccediACanaliEChat() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AREA COMUNICAZIONI ---" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();
            ViewUtils.println("1. Accedi ai Canali di Progetto");
            ViewUtils.println("2. Accedi alle Chat Private");
            ViewUtils.println("0. Torna alla Home Principale");
            ViewUtils.printSeparator();
            int scelta = InputUtils.readInt("Seleziona un'opzione: ");
            try {
                switch (scelta) {
                    case 1:
                        handleAccessoCanaliProgetto();
                        break;
                    case 2:
                        handleAccessoChatPrivate();
                        break;
                    case 0:
                        return;
                    default:
                        ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("");
                }
            } catch (CommandException e) {
                // Cattura il /b e ripropone il menu
            }
        }
    }

    private void handleAccessoCanaliProgetto() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- ACCESSO AI CANALI DI PROGETTO ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare al menu precedente.");
        ViewUtils.printSeparator();
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);
        if (mieiCanali.isEmpty()) {
            ViewUtils.println("Non partecipi a nessun canale di progetto.");
            InputUtils.pressEnterToContinue("");
            return;
        }
        String prompt = "Seleziona un canale per visualizzarne i messaggi:";
        int idCanaleSelezionato = selezionaCanale(mieiCanali, prompt);
        new ChatView(interazioneController, idCanaleSelezionato, TipoContestoChat.CANALE_PROGETTO, false, idUtenteLoggato).show();
    }

    /**
     * Gestisce l'accesso all'area delle chat private, presentando un sotto-menu
     * per visualizzare le chat esistenti o avviarne una nuova.
     * @throws CommandException se l'utente sceglie di tornare indietro.
     */
    private void handleAccessoChatPrivate() throws CommandException {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AREA CHAT PRIVATE ---" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();
            ViewUtils.println("1. Visualizza le tue chat");
            ViewUtils.println("2. Avvia una nuova chat da un messaggio di un canale");
            ViewUtils.printSeparator();
            ViewUtils.println("0. Torna al menu precedente");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");

            switch (scelta) {
                case 1:
                    handleVisualizzaMieChat();
                    break;
                case 2:
                    handleAvviaNuovaChatDaCanale();
                    break;
                case 0:
                    return; // Esce e torna al menu delle comunicazioni
                default:
                    ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("");
            }
        }
    }

    /**
     * Mostra la lista delle chat private dell'utente e permette di selezionarne
     * una per visualizzarne la conversazione.
     * @throws CommandException se l'utente usa /b.
     */
    private void handleVisualizzaMieChat() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- LE TUE CHAT PRIVATE ---" + ColorUtils.ANSI_RESET);
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<ChatPrivataDTO> mieChat = interazioneController.getMieChatPrivate(idUtenteLoggato);

        if (mieChat.isEmpty()) {
            ViewUtils.println("Non hai nessuna chat privata attiva.");
            InputUtils.pressEnterToContinue("Premi Invio per tornare indietro...");
            return;
        }

        ViewUtils.println("Elenco delle tue conversazioni private:");
        String header = String.format("%-5s | %-20s | %s", "ID", "Avviata il", "Conversazione con");
        ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);

        mieChat.forEach(chat -> {
            String riga = String.format("%-5d | %-20s | %s",
                    chat.getIdChat(), chat.getDataCreazione(), chat.getAltroPartecipanteUsername());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        // Aggiungiamo il tipo ChatPrivataDTO a InputHelper
        int idChatSelezionata = InputHelper.chiediIdValido("Inserisci l'ID della chat da visualizzare: ", mieChat);

        new ChatView(
                interazioneController,
                idChatSelezionata,
                TipoContestoChat.CHAT_PRIVATA,
                false, // Non è in sola lettura, puoi partecipare
                idUtenteLoggato
        ).show();
    }

    /**
     * Guida l'utente attraverso il processo di avvio di una nuova chat privata,
     * partendo dalla selezione di un canale e di un messaggio.
     * @throws CommandException se l'utente usa /b.
     */
    private void handleAvviaNuovaChatDaCanale() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AVVIA NUOVA CHAT PRIVATA ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);

        if (mieiCanali.isEmpty()) {
            ViewUtils.println("Devi partecipare ad almeno un canale per poter avviare una chat.");
            InputUtils.pressEnterToContinue("");
            return;
        }

        // La CommandException dalla selezione del canale viene gestita dal chiamante
        String promptCanale = "Seleziona il canale contenente il messaggio da cui partire:";
        int idCanaleSelezionato = selezionaCanale(mieiCanali, promptCanale);

        // Mostriamo solo la prima pagina di messaggi per la selezione
        List<MessaggioDTO> messaggi = interazioneController.getPaginaMessaggiCanale(idCanaleSelezionato, idUtenteLoggato, 1);

        if (messaggi.isEmpty()) {
            ViewUtils.println("Questo canale non ha messaggi da cui avviare una chat.");
            InputUtils.pressEnterToContinue("");
            return;
        }

        ViewUtils.println("\n--- Messaggi del Canale (Pagina 1) ---");
        messaggi.forEach(msg -> {
            String riga = String.format("ID %-4d | [%s] %s: %s",
                    msg.getIdMessaggio(), new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(msg.getTimestamp()),
                    msg.getUsernameMittente(), msg.getContenuto());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        ViewActionHelper.avviaChatPrivataDaListaMessaggi(messaggi, idUtenteLoggato, this.interazioneController);
        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu precedente...");
    }


    private int selezionaProgetto(List<Progetto> progetti, String prompt) throws CommandException {
        ViewUtils.println(prompt);
        progetti.forEach(p -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Progetto: ", progetti);
    }

    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", c.getIdCanale(), c.getNomeCanale())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Canale: ", canali);
    }
}
