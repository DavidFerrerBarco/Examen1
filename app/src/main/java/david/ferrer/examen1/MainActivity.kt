package david.ferrer.examen1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import david.ferrer.examen1.ui.theme.Examen1Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen1Theme {
                MyScaffold()
            }
        }
    }
}

@Composable
fun MyScaffold(){
    val scaffoldState = rememberScaffoldState()
    var vista by remember { mutableStateOf(true) }

    var listaPeliculas by remember{ mutableStateOf(mutableListOf<Pelicula>()) }
    var listaFavoritas by remember{ mutableStateOf(mutableListOf<Pelicula>()) }

    listaPeliculas.add(
        Pelicula(
            "Black Adam",
            "Trata sobre Black Adam, el personaje homónimo de DC Comics",
            6,
            92,
            R.drawable.ic_launcher_background,
            R.drawable.blackadam
        )
    )

    listaPeliculas.add(
        Pelicula(
            "Doctor Who",
            "El Doctor es un extraterrestre que viaja en el espacio y el tiempo",
            9,
            42,
            R.drawable.ic_launcher_background,
            R.drawable.doctorwho
        )
    )

    listaPeliculas.add(
        Pelicula(
            "La casa del dragón",
            "La historia de la familia Targaryen 200 años antes de los eventos que tuvieron lugar en \"Juego de tronos\"",
            7,
            61,
            R.drawable.ic_launcher_background,
            R.drawable.lacasadeldragon
        )
    )

    listaPeliculas.add(
        Pelicula(
            "Kantara",
            "La película está protagonizada por Shetty como una campeona de Kambala que está en desacuerdo con un oficial DRFO vertical, Murali.",
            4,
            23,
            R.drawable.ic_launcher_background,
            R.drawable.kantara
        )
    )

    listaPeliculas.add(
        Pelicula(
            "Anillos del poder",
            "Los héroes se enfrentan al temido resurgimiento del mal en la Tierra Media, forjando legados que perdurarán mucho tiempo después de su desaparición.",
            6,
            80,
            R.drawable.ic_launcher_background,
            R.drawable.anillosdelpoder
        )
    )

    listaPeliculas.add(
        Pelicula(
            "El Cuarto pasajero",
            "una película española de comedia dirigida por Álex de la Iglesia y protagonizada por Blanca Suárez, Alberto San Juan, Ernesto Alterio y Rubén Cortada.",
            1,
            2,
            R.drawable.ic_launcher_background,
            R.drawable.cuartopasajero
        )
    )

    Scaffold(
        topBar = { MyTopAppBar()},
        scaffoldState = scaffoldState,
        bottomBar = { MyBottomNavigation({vista = it})},
        isFloatingActionButtonDocked = false,
        content = { MyContent(vista, listaPeliculas, listaFavoritas,{listaFavoritas.add(it)}, {listaFavoritas.remove(it)})}
    )
}

@Composable
fun MyTopAppBar(){
    TopAppBar(
        title = {
            Text(text = "MonkeyFilms")
        },
        backgroundColor = Color(0xFF7d2181),
        contentColor = Color.White,
        elevation = 123.dp,
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Abrir menú desplegable")
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
            }
        }
    )
}

@Composable
fun MyContent(
    vista: Boolean,
    listaPeliculas: List<Pelicula>,
    listaFavoritas: List<Pelicula>,
    newFavoritos: (Pelicula) -> Unit,
    deleteFavorite: (Pelicula) -> Unit
){
    var mostrarLogIn by rememberSaveable { mutableStateOf(true) }
    var user by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var mostrarRegistro by rememberSaveable { mutableStateOf(false)}

    MyDialog(
        show = mostrarLogIn,
        showChange = { mostrarLogIn = !mostrarLogIn},
        user = user,
        userChange = {user = it},
        password = password,
        passwordChange = {password = it},
        registroChange = {mostrarRegistro = !mostrarRegistro}
    )
    if(mostrarRegistro){
        MyRegistro(
            mostrarRegistroChange = { mostrarRegistro = !mostrarRegistro}
        )
    }
    if(!mostrarRegistro && !mostrarLogIn){
        if(vista){
            MyContentHome(listaPeliculas,listaFavoritas,{newFavoritos(it)})
        }else{
            MyContentFavoritos(listaFavoritas, {deleteFavorite(it)})
        }
    }

}

@Composable
fun MyDialog(
    show: Boolean,
    showChange:() -> Unit,
    user: String,
    userChange: (String) -> Unit,
    password: String,
    passwordChange: (String) -> Unit,
    registroChange: () -> Unit
){
    var passwordVisible by remember{ mutableStateOf(false) }

    if (show){
        Dialog(onDismissRequest ={showChange()},
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(24.dp)
                    .fillMaxWidth()
            ){
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "usuario",
                        tint = Color(0xFF7d2181),
                        modifier = Modifier.size(50.dp)
                    )
                    MyDialogTextFields(
                        texto = "Usuario/ Email",
                        name = user,
                        onValueChange = userChange,
                        200.dp,
                        true
                    )
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "password",
                        tint = Color(0xFF7d2181),
                        modifier = Modifier.size(50.dp)
                    )
                    MyDialogTextFields(
                        texto = "Password",
                        name = password,
                        onValueChange = passwordChange,
                        150.dp,
                        passwordVisible)
                    Button(
                        onClick = {passwordVisible = !passwordVisible},
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        modifier = Modifier.size(60.dp)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.visible),
                            contentDescription = "visible",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(top = 12.dp))

                Button(
                    onClick = {
                        if(user == "David" && password == "1234"){
                            showChange()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7d2181))
                ) {
                    Text(
                        "Entrar",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        textAlign = TextAlign.Center)
                }

                TextButton(
                    onClick = {registroChange(); showChange()},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Aún no te has registrado?",
                        style = TextStyle(textDecoration = TextDecoration.Underline),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun MyDialogTextFields(
    texto: String,
    name: String,
    onValueChange: (String) -> Unit,
    valor: Dp,
    visible : Boolean){
    Column {
        TextField(
            value = name,
            onValueChange = onValueChange,
            placeholder = {Text(text=texto)},
            modifier = Modifier.width(valor),
            visualTransformation =
            if (visible) VisualTransformation.None
            else PasswordVisualTransformation()
        )
    }
}

@Composable
fun MyBottomNavigation(vistaHomeChange: (Boolean) -> Unit){

    val tamanyo = 33.dp

    BottomNavigation(
        backgroundColor = Color(0xFF7d2181),
        contentColor = Color.White,
    ) {
        BottomNavigationItem(selected = false, onClick = { vistaHomeChange(true)}, icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "home",
                modifier = Modifier.size(tamanyo)
            )
        }, label = { Text("Home", fontSize = 12.sp) })
        BottomNavigationItem(selected = false, onClick = { vistaHomeChange(false) }, icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "fav",
                modifier = Modifier.size(tamanyo)
            )
        }, label = { Text("Favoritas", fontSize = 12.sp) })
        BottomNavigationItem(selected = false, onClick = { }, icon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "profile",
                modifier = Modifier.size(tamanyo)
            )
        }, label = { Text("Profile", fontSize = 12.sp) })
    }
}


@Composable
fun MyRegistro(mostrarRegistroChange: ()-> Unit){

    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var contraseña2 by remember { mutableStateOf("") }

    val maxChar = 50
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.padding(30.dp))

        MyRegistroTextField(
            texto = usuario,
            onValueChange = {usuario = it},
            label = "Usuario",
            maxChar = maxChar,
            visible = true
        )
        MyRegistroText(texto = usuario, maxChar = maxChar)
        //---------------------------------
        MyRegistroTextField(
            texto = email,
            onValueChange = {email = it},
            label = "Email",
            maxChar = maxChar,
            visible = true
        )
        MyRegistroText(texto = email, maxChar = maxChar)
        //---------------------------------
        MyRegistroTextField(
            texto = contraseña,
            onValueChange = {contraseña = it},
            label = "Contraseña",
            maxChar = maxChar,
            visible = false
        )
        MyRegistroText(texto = contraseña, maxChar = maxChar)
        //---------------------------------
        MyRegistroTextField(
            texto = contraseña2,
            onValueChange = {contraseña2 = it},
            label = "Repite la contraseña",
            maxChar = maxChar,
            visible = false
        )
        MyRegistroText(texto = contraseña2, maxChar = maxChar)

        //-----------------------------------------------------
        Spacer(modifier = Modifier.padding(10.dp))
        var myDeporte by rememberSaveable { mutableStateOf(false) }
        var myAccion by rememberSaveable { mutableStateOf(false) }
        var mySiFi by rememberSaveable { mutableStateOf(false) }
        var myRomance by rememberSaveable { mutableStateOf(false) }
        var myHistorica by rememberSaveable { mutableStateOf(false) }
        var myDocumental by rememberSaveable { mutableStateOf(false) }
        //-----------------------------------------------------
        Text("Intereses", modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)){
            Column {
                MyCheckBox("Deportes", state = myDeporte){myDeporte = it}
                MyCheckBox("Acción", state = myAccion){myAccion = it}
                MyCheckBox("Si-Fi", state = mySiFi){mySiFi = it}
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Column {
                MyCheckBox("Romance", state = myRomance){myRomance = it}
                MyCheckBox("Históricas", state = myHistorica){myHistorica = it}
                MyCheckBox("Documentales", state = myDocumental){myDocumental = it}
            }
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Button(
            onClick = {
                      if((contraseña == contraseña2) && usuario.isNotEmpty() && email.isNotEmpty() && contraseña.isNotEmpty())
                            mostrarRegistroChange()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7d2181)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Text(
                "Registrarse",
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun MyRegistroTextField(
    texto: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxChar: Int,
    visible: Boolean
){

    TextField(
        value = texto,
        onValueChange = {
            if(it.length <= maxChar){
                onValueChange(it)
            }
        },
        label = { Text(text = label)},
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFF7d2181),
            unfocusedIndicatorColor = Color(0xFF7d2181),
            focusedLabelColor = Color.Gray
        ),
        visualTransformation =
        if (visible) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

@Composable
fun MyRegistroText(texto: String, maxChar: Int){
    Text(
        text = "${texto.length} / $maxChar",
        textAlign = TextAlign.End,
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    )
}

@Composable
fun MyCheckBox(texto: String,state: Boolean, onCheckedChange: (Boolean) -> Unit){
    Row(modifier = Modifier.padding(2.dp)) {
        Checkbox(
            checked = state,
            onCheckedChange = { onCheckedChange(!state) },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF7d2181)))
        Spacer(modifier = Modifier.width(2.dp))
        Text(texto, modifier = Modifier.padding(vertical = 12.dp))
    }
}

@Composable
fun MyContentHome(listaPeliculas: List<Pelicula>,listaFavoritas: List<Pelicula>, newFavoritos: (Pelicula) -> Unit){
    Column(
        Modifier.padding(start = 2.dp)
    ){
        listaPeliculas.forEach{ pelicula ->
            var vista  by remember { mutableStateOf(false) }
            if(!vista)
                MyFirstCardHome(pelicula,listaFavoritas, {vista = !vista}, {newFavoritos(it)})
            else
                MySecondCardHome(pelicula){vista = !vista}
            Spacer(modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}

@Composable
fun MyContentFavoritos(listaFavoritas: List<Pelicula>, deleteFavorite: (Pelicula) -> Unit){
    Column(
        Modifier.padding(start = 2.dp)
    ){
        listaFavoritas.forEach{ pelicula ->
            MyCardFavorite(pelicula,{deleteFavorite(it)})
            Spacer(modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyFirstCardHome(pelicula: Pelicula, listaFavoritas: List<Pelicula>, vistaChange: () -> Unit, newFavoritos: (Pelicula) -> Unit){
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        onClick = {vistaChange()}
    ){
        Row(
            Modifier.padding(start = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.padding(5.dp))
            Image(
                painterResource(id = pelicula.imagen),
                contentDescription = "foto",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column(
                Modifier
                    .padding(start = 10.dp)
                    .width(120.dp)){
                Text(pelicula.titulo)
                Row{
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        Modifier
                            .size(20.dp)
                            .padding(top = 1.dp)
                    )
                    Text(
                        pelicula.estrellas.toString(),
                        fontSize = 15.sp,
                    )
                }
            }
            Row{
                Button(
                    onClick = {
                        if(!existeEnFavoritas(pelicula, listaFavoritas))
                            newFavoritos(pelicula)
                              },
                    modifier = Modifier
                        .padding(start = 140.dp)
                        .clip(CircleShape)
                        .size(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                ) {
                    Text("+", fontSize = 15.sp, color = Color.White)
                }
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MySecondCardHome(pelicula: Pelicula, vistaChange: () -> Unit) {

    val checkedState = remember { mutableStateOf(false) }

    Card(
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        onClick = {vistaChange()}
    ){
        Column{
            
            Image(
                painter = painterResource(id = pelicula.fondo),
                contentDescription = "fondo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .height(100.dp),
                contentScale = ContentScale.FillWidth
            )

            Row(
                Modifier.padding(start = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.padding(5.dp))
                Image(
                    painterResource(id = pelicula.imagen),
                    contentDescription = "foto",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Column(
                    Modifier
                        .padding(start = 10.dp)
                        .width(200.dp)){
                    Text(pelicula.titulo)
                    Text(pelicula.descripcion, fontSize = 12.sp, overflow = TextOverflow.Ellipsis, maxLines = 2 )
                }
                Spacer(modifier = Modifier.padding(start = 60.dp))

                Column{
                    Row{
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            Modifier
                                .size(20.dp)
                                .padding(top = 1.dp)
                        )
                        Text(
                            pelicula.estrellas.toString(),
                            fontSize = 15.sp,
                        )
                    }
                    Row {
                        IconToggleButton(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = !checkedState.value
                            },
                            modifier = Modifier
                                .padding(1.dp)
                                .size(20.dp)
                        ) {
                            val transition = updateTransition(checkedState.value)
                            val tint by transition.animateColor(label = "iconColor") { isCheked ->
                                if (isCheked) Color.Red else Color.Black
                            }
                            val size by transition.animateDp(
                                transitionSpec = {
                                    if (false isTransitioningTo true) {
                                        keyframes {
                                            durationMillis = 250
                                            30.dp at 0 with LinearOutSlowInEasing
                                            35.dp at 15 with FastOutLinearInEasing
                                            40.dp at 75
                                            35.dp at 150
                                        }
                                    } else {
                                        spring(stiffness = Spring.StiffnessVeryLow)
                                    }
                                },
                                label = "Size"
                            ) { 17.dp }

                            Icon(
                                imageVector = if (checkedState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Icon",
                                tint = tint,
                                modifier = Modifier.size(size)
                            )
                        }

                        Text(text = if (checkedState.value) (pelicula.corazones + 1).toString() else (pelicula.corazones).toString())
                    }
                }
            }
        }
        
    }
}

@Composable
fun MyCardFavorite(pelicula: Pelicula, deleteFavorite: (Pelicula) -> Unit){
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ){
        Row(
            Modifier.padding(start = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.padding(5.dp))
            Image(
                painterResource(id = pelicula.imagen),
                contentDescription = "foto",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column(
                Modifier
                    .padding(start = 10.dp)
                    .width(270.dp)){
                Text(pelicula.titulo)
                Row{
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        Modifier
                            .size(20.dp)
                            .padding(top = 1.dp)
                    )
                    Text(
                        pelicula.estrellas.toString(),
                        fontSize = 15.sp,
                    )
                }
            }
            IconButton(onClick = { deleteFavorite(pelicula) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete",
                    tint = Color.Red,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

fun existeEnFavoritas(pelicula: Pelicula, listaFavoritas: List<Pelicula>): Boolean{
    for(favorita in listaFavoritas){
        if(favorita.equals(pelicula))
            return true
    }
    return false
}

