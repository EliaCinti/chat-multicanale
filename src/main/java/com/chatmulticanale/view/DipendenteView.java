package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

public class DipendenteView implements View {

    private final InterazioneUtenteController interazioneController;
    private final ComunicazioneViewHelper comunicazioneHelper;

    public DipendenteView(InterazioneUtenteController interazioneController) {
        this.interazioneController = interazioneController;
        this.comunicazioneHelper = new ComunicazioneViewHelper(interazioneController);
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_DIPENDENTE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.WELCOME + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println(CostantiView.HOME_DIPENDENTE_OPZIONE_1);
            ViewUtils.println(CostantiView.HOME_DIPENDENTE_OPZIONE_0);
            ViewUtils.printSeparator();

            int scelta;
            try {
                scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 1);
            } catch (CommandException e) {
                if (e.getNavigazione().azione == Navigazione.Azione.LOGOUT) {
                    return e.getNavigazione();
                }
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
                continue;
            }
            switch (scelta) {
                case 1:
                    handleAccediACanaliEChat();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
                default:
            }
        }
    }

    private void handleAccediACanaliEChat() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AREA_COMUNICAZIONI + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.ACCEDI_A_CANALI_E_CHAT_OPZIONE_1);
            ViewUtils.println(CostantiView.ACCEDI_A_CANALI_E_CHAT_OPZIONE_2);
            ViewUtils.printSeparator();

            try {
                int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 1, 2);
                int idUtente = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

                switch (scelta) {
                    case 1:
                        comunicazioneHelper.handleAccessoCanaliProgetto(idUtente);
                        break;
                    case 2:
                        comunicazioneHelper.handleAccessoChatPrivate(idUtente);
                        break;
                    default:
                }
            } catch (CommandException e) {
                return;
            }
        }
    }
}
