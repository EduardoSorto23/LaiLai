package net.dsdev.lailai.clientes.util;

public class Constants {


    /**
    * Strings for Retrofit instances
    * */

    public static final String baseUrl = "http://173.254.242.213:8080/clientapp-web/webresources/";
    // public static final String baseUrl = "http://192.168.0.130:8080/clientapp-web/webresources/";
    public static final String findById = "getMenuDetail/{id}";
    public static final String findAll = "getMenus/APP";
    //public static final String sendOrder = "sendOrder";
    public static final String sendOrder = "order/register";
    public static final String getPromos = "getMenusPromo/APP";
    public static final String loginLocalService = "account/login/local";
    public static final String loginApiService = "account/login/api";
    public static final String resetPassword = "account/resetPassword";
    public static final String createNewUser = "account/save";
    public static final String listDirections = "direccion/list/{id}";
    public static final String saveAddress = "direccion/save";
    public static final String changePassword = "account/changePassword";
    public static final String updateAddress = "direccion/update";
    public static final String deleteAddress = "direccion/delete/{id}";
    public static final String saveCard = "card/save";
    public static final String getCards = "card/list/{id}";
    public static final String deleteCard = "card/delete/{id}";
    public static final String updateCard = "card/update";
    public static final String getActiveOrders = "order/activas/{idClient}";
    public static final String getActiveOrderDetail = "order/{idOrder}";
    public static final String getVentaSugerida = "sugerido/menus";

    public static final String getStores = "tiendas/list";
    public static final String getCoverage = "tiendas/cobertura/{id}";
    public static final String orderCancel = "order/cancelar/{idOrder}";

    public static final String getDepartments = "direccion/departamentos";
    public static final String getMunicipalies = "direccion/municipios/{idDepto}";
    /*
    * End to strings for Retrofit
    */
    public static final String id = "id";
    public static final String name = "nombre";
    public static final String desc = "descripcion";
    public static final String price = "precio";
    public static final String state = "estado";
    public static final String categories = "categorias";
    public static final String image = "imagen";
    public static final String small = "small";
    public static final String normal = "normal";
    public static final String large = "large";
    public static final String unavailable = "unavailable";
    public static final String menus = "menus";
    public static final String idVariant = "idVariante";
    public static final String isDefault = "default";
    public static final String extraPrice = "precioExtra";
    public static final String variant = "variante";
    public static final String variants = "variantes";
    public static final String question = "pregunta";
    public static final String idOption = "idOpcion";
    public static final String options = "opciones";
    public static final String idMenu = "idMenu";
    public static final String menu = "menu";
    public static final String selectedVariants = "varianteSeleccionada";
    public static final String finalPrice = "precioFinal";
    public static final String idProduct = "idProducto";
    public static final String idClient = "idCliente";
    public static final String idTelephone = "idTelefono";
    public static final String idDirection = "idDireccion";
    public static final String indications = "indicaciones";
    public static final String direction = "direccion";
    public static final String canal = "canal";
    public static final String amount = "monto";
    public static final String quantity = "cantidad";
    public static final String unitPrice = "precioUnitario";
    public static final String courtesy = "cortesia";
    public static final String paymentMethod = "formaPago";
    public static final String document = "documento";
    public static final String expiration = "vencimiento";
    public static final String ownerName = "nombreTitular";
    public static final String ccv = "ccv";
    public static final String paymentDetail = "detallePago";
    public static final String promos = "promos";
    public static final String images = "imagenes";
    public static final String recommended = "recomendado";

    // String for users request
    public static final String names = "nombres";
    public static final String lastNames = "apellidos";
    public static final String users = "usuarios";
    public static final String valid = "result";
    public static final String client = "client";
    public static final String user = "usuario";
    public static final String password = "clave";
    public static final String accountType = "tipoCuenta";
    public static final String email = "email";
    public static final String msg = "msg";
    public static final String gender = "genero";

    public static final String menuTree = "menuTree";

    public static final int HEADER = 0;
    public static final int ITEM = 1;

    public static final String complete = "completado";

    public static final String department = "departamento";
    public static final String municipaly = "municipio";
    public static final String latitude = "latitud";
    public static final String longitude = "longitud";

    public static final String addresses = "direcciones";
    public static final String idAddress = "idDireccion";

    public static final String token = "token";
    public static final String year = "anioVencimiento";
    public static final String month = "mesVencimiento";
    public static final String documentId = "numeroDocumento";
    public static final String cardId = "idTarjeta";
    public static final String owner = "titular";
    public static final String cards = "tarjetas";
    public static final String telephone = "telefono";
    public static final String orderNumber = "orderNumber";
    public static final String orderCode = "codigoPedido";
    public static final String orderDate = "fecha";
    public static final String orderHour= "hora";
    public static final String orderTotal = "totalOrden";
    public static final String orders = "ordenes";
    public static final String code = "codigo";
    public static final String openHour = "horaApertura";
    public static final String closeHour = "horaCierre";
    public static final String stores = "tiendas";
    public static final String idStore = "idTienda";
    public static final String account = "tipo_cuenta";
    public static final String chance = "ocasion";

    public static final String references = "referencias";
    public static final String suBurb = "colonia";
    public static final String zone = "zona";
    public static final String accessCode = "codigoAcceso";
    public static final String houseNumber = "numeroCasa";
    public static final String departments = "departamentos";
    public static final String municipalies = "municipios";
    public static final String result = "result";
    public static final String message = "msg";
    public static final String stateCoverageClose = "CER";
    public static final String stateCoverageOpen = "ACT";
    public static final String stateCoverageOut = "NCB";
    public static final String fecHourCoverage = "fechaHora";
    public static final String futureOrderCoverage = "pedidoFuturo";
    public static final String accountFacebook = "FBK";
    public static final String accountGoogle = "GOG";
    public static final String ocasionVsug = "DOM";
    public static final String canalVsug = "APP";
}
