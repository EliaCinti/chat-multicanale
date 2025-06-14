package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.utils.ViewActionHelper;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

import java.text.SimpleDateFormat;
import java.util.List;

public class DipendenteView implements View {

    private final InterazioneUtenteController interazioneController;

    public DipendenteView(InterazioneUtenteController interazioneController) {
        this.interazioneController = interazioneController;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- HOME DIPENDENTE ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println("1. Accedi ai tuoi canali e chat");
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");

            switch (scelta) {
                case 1:
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
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- I MIEI CANALI DI PROGETTO ---" + ColorUtils.ANSI_RESET);
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

        new ChatView(
                interazioneController,
                idCanaleSelezionato,
                TipoContestoChat.CANALE_PROGETTO,
                false, // Non è in sola lettura
                idUtenteLoggato
        ).show();
    }

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

            try {
                switch (scelta) {
                    case 1:
                        handleVisualizzaMieChat();
                        break;
                    case 2:
                        handleAvviaNuovaChatDaCanale();
                        break;
                    case 0:
                        return;
                    default:
                        ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("");
                }
            } catch (CommandException e) {
                // Cattura il /b e ripropone il sotto-menu
            }
        }
    }

    private void handleVisualizzaMieChat() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- LE TUE CHAT PRIVATE ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare al menu precedente.");
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

        int idChatSelezionata = InputHelper.chiediIdValido("Inserisci l'ID della chat da visualizzare: ", mieChat);

        new ChatView(
                interazioneController,
                idChatSelezionata,
                TipoContestoChat.CHAT_PRIVATA,
                false, // Non è in sola lettura
                idUtenteLoggato
        ).show();
    }

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

        String promptCanale = "Seleziona il canale contenente il messaggio da cui partire:";
        int idCanaleSelezionato = selezionaCanale(mieiCanali, promptCanale);

        List<MessaggioDTO> messaggi = interazioneController.getPaginaMessaggiCanale(idCanaleSelezionato, idUtenteLoggato, 1);

        if (messaggi.isEmpty()) {
            ViewUtils.println("Questo canale non ha messaggi da cui avviare una chat.");
            InputUtils.pressEnterToContinue("");
            return;
        }

        ViewUtils.println("\n--- Messaggi del Canale (Pagina 1) ---");
        messaggi.forEach(msg -> {
            String riga = String.format("ID %-4d | [%s] %s: %s",
                    msg.getIdMessaggio(), new SimpleDateFormat("dd-MM-yyyy HH:mm").format(msg.getTimestamp()),
                    msg.getUsernameMittente(), msg.getContenuto());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        ViewActionHelper.avviaChatPrivataDaListaMessaggi(messaggi, idUtenteLoggato, this.interazioneController);

        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu precedente...");
    }

    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", c.getIdCanale(), c.getNomeCanale())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Canale: ", canali);
    }
}
