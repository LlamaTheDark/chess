package service.game.play;

import exchange.fullduplex.websocket.commands.UserGameCommand;
import exchange.fullduplex.websocket.messages.ServerMessage;

public
interface GamePlayService<M extends ServerMessage, C extends UserGameCommand> {
    M serve(C command);
}
