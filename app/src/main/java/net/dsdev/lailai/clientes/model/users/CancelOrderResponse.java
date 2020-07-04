package net.dsdev.lailai.clientes.model.users;

import net.dsdev.lailai.clientes.model.Request.Order;

import java.io.Serializable;

public class CancelOrderResponse extends AuthResponse implements Serializable {

    private Order orden;

    public Order getOrden() {
        return orden;
    }

    public void setOrden(Order orden) {
        this.orden = orden;
    }
}
