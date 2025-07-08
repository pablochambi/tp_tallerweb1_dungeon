// function comprar(button) {
//
//     const mp = new MercadoPago('APP_USR-6674ddbc-26cd-49a4-9245-fb430b35e6fd');
//
//     // Obtener el ID de la criptomoneda del botón
//     let paquete = button.getAttribute('data-id');
//     // Obtener el valor de la suscripcion usando su ID, y quitar el símbolo $.
//     let valor = document.getElementById('monto').value;
//
//     console.log(paquete)
//     console.log(valor)
//
//     // Llamada al servidor para crear la preferencia
//     fetch(`/spring/crear-preferencia`,{
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/x-www-form-urlencoded'
//         },
//         body: `paqueteOro=${paquete}&monto=${valor}`
//     })
//         .then(response => {
//             console.log("estoy adentro del then antes del if");
//             if (!response.ok) {
//                 throw new Error('Network response was not ok');
//             }
//             return response.text();
//         })
//         .then(initPoint => {
//             window.location.href = initPoint;
//         })
//         .catch(error => console.error('Error:', error));
// }


function comprar(paqueteId, precio) {
    console.log("clicked comprar", paqueteId, precio);
    const mp = new MercadoPago('APP_USR-6674ddbc-26cd-49a4-9245-fb430b35e6fd');

    fetch(`/spring/crear-preferencia`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `paqueteOro=${paqueteId}&monto=${precio}`
    })
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.text();
        })
        .then(initPoint => {
            window.location.href = initPoint;
        })
        .catch(error => console.error('Error:', error));
}

// function comprar(button) {
//     console.log("mercadoPago.js cargado");
//     const mp = new MercadoPago('APP_USR-6674ddbc-26cd-49a4-9245-fb430b35e6fd');
//
//     let paquete = button.getAttribute('data-id');
//     let valor = document.getElementById('monto').value;
//
//     fetch('/crear-preferencia', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/x-www-form-urlencoded'
//         },
//         body: `paqueteOro=${paquete}&monto=${valor}`
//     })
//         .then(response => {
//             if (!response.ok) throw new Error('Fallo la llamada');
//             return response.text();
//         })
//         .then(url => window.location.href = url)
//         .catch(err => console.error("Error al crear preferencia:", err));
// }