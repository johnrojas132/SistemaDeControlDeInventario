# SistemaDeControlDeInventario

Sistema de Inventario 
John Rojas Alvarado 
Profesor: José Andrés Jiménez Zamora 
https://github.com/johnrojas132/SistemaDeControlDeInventario.git

Descripción general
Aplicación de escritorio en Java (Swing) para gestionar un inventario simple. Permite registrar, listar, editar y eliminar productos, ver estadísticas básicas,ordenar y exportar el inventario a CSV. 

Requisitos para ejecutar
•	Ejecutar la clase principal: sistemadecontroldeinventario.
•	No requiere base de datos se ejecuta a partir del mainframe interactuando con todos los paneles.

Componentes Swing utilizados
JFrame, JPanel, JTabbedPane, JToolBar, JTable, JFileChooser, JOptionPane, JTextArea, JScrollPane, JComboBox, JButton, JLabel, JTextField, JCheckBox.

Collections y utilidades usadas
List / ArrayList, Set / HashSet, Map / HashMap, Stack, Iterator, Collections ordenamiento.

Excepciones personalizadas
•	ArchivoException — errores  al exportar.
•	DatoInvalidoException — validaciones de negociom, campos obligatorios, formatos.
•	ProductoDuplicadoException — intento de agregar un producto con codigo ya existente.
Estructura de clases 
Clases principales con una descripción.
•	modelo. Producto Representa un producto con atributos: id, codigo, nombre, precio, categoria, cantidad, disponible, descripcion. Normaliza cadenas y define equals/hashCode por codigo.
•	repositorio. ProductoRepositorio Repositorio en memoria que guarda productos. Implementa CRUD sincronizado, control de códigos únicos, generación automática de IDs, contador por categoría e historial de acciones (Stack). Incluye método limpiar
•	negocio. ProductoNegocio Capa de negocio que valida productos precio > 0, cantidad ≥ 0, campos obligatorios, lanza excepciones apropiadas, delega persistencia al repositorio y expone búsquedas, filtros, ordenamientos y estadísticas.
•	excepciones. ArchivoException Excepción para errores relacionados con operaciones de archivo exportar.
•	excepciones. DatoInvalidoException Excepción para entradas inválidas desde la UI o la capa de negocio.
•	excepciones. ProductoDuplicadoException Señala intentos de agregar un producto con código ya existente.
•	util. ArchivoUtil Utilidad para exportar inventario a CSV usando JFileChooser. Normaliza y escapa campos, muestra mensajes al usuario y lanza ArchivoException en errores.
•	presentación. registroPanel Panel Swing con formulario para crear/editar productos. Valida campos, maneja botones Guardar y Limpiar y notifica a ProductoPanel y EstadisticaPanel.
•	presentación. ProductoPanel Panel con tabla de productos: listar, buscar por código/nombre, filtrar por categoría, ordenar, editar y eliminar. doble clic para editar.
•	presentación. EstadisticaPanel Panel que muestra métricas: total productos, unidades, valor inventario, mayor/menor precio y conteo por categoría. Botón para ver historial y refrescar.

•	MainFrame Ventana principal que arma las pestañas, toolbar y menús. conecta con ProductoNegocio y ofrece exportar, ordenar y ver historial.
•	sistemadecontroldeinventario. Clase main que arranca la UI en el Event Dispatch Thread.
Instrucciones de uso 
1.	Registro
o	Completar: Código, Nombre, Categoría, Cantidad, Precio, Estado, Descripción.
o	Presionar Guardar. Validaciones: código y nombre obligatorios; precio > 0; cantidad ≥ 0.
2.	Productos
o	Revisar tabla; usar botones para Buscar código/nombre, Filtrar categoría y Ordenar nombre/precio/cantidad.
o	Seleccionar fila y presionar Editar o Eliminar. Doble clic abre edición.
3.	Estadísticas
o	Pulsar Refrescar para recalcular métricas. Historial muestra acciones realizadas agregado/editar/eliminar.
4.	Exportar
o	Ir a Archivo  Exportar o usar el botón de toolbar para guardar CSV con el inventario actual.
