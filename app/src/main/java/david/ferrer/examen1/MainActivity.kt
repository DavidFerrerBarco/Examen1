package david.ferrer.examen1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
    Scaffold(
        topBar = { MyTopAppBar()},
        scaffoldState = scaffoldState,
        bottomBar = { MyBottomNavigation()},
        isFloatingActionButtonDocked = false,
        content = { MyContent()}
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
fun MyContent(){
    var mostrar by rememberSaveable { mutableStateOf(true) }
    var user by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    MyDialog(
        show = mostrar,
        showChange = { mostrar = !mostrar},
        user = user,
        userChange = {user = it},
        password = password,
        passwordChange = {password = it}
    )

}

@Composable
fun MyDialog(show: Boolean, showChange:() -> Unit,user: String, userChange: (String) -> Unit, password: String, passwordChange: (String) -> Unit){
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
                    Icon(imageVector = Icons.Rounded.Person, contentDescription = "usuario", tint = Color(0xFF7d2181), modifier = Modifier.size(50.dp))
                    MyDialogTextFields(texto = "Usuario/ Email", name = user, onValueChange = userChange, 200.dp, true)
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row {
                    Icon(imageVector = Icons.Rounded.Lock, contentDescription = "password", tint = Color(0xFF7d2181), modifier = Modifier.size(50.dp))
                    MyDialogTextFields(texto = "Password", name = password, onValueChange = passwordChange, 150.dp, passwordVisible)
                    Button(
                        onClick = {passwordVisible = !passwordVisible},
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ){
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "blind", tint = Color(0xFF7d2181), modifier = Modifier.size(50.dp))
                    }
                }
                Spacer(modifier = Modifier.padding(top = 12.dp))

                Button(
                    onClick = {
                        if(user.isNotEmpty() && password.isNotEmpty()){
                            showChange()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7d2181))
                ) {
                    Text("Entrar", modifier = Modifier.fillMaxWidth(), color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun MyDialogTextFields(texto: String, name: String,  onValueChange: (String) -> Unit, valor: Dp, visible : Boolean){
    Column {
        TextField(value = name, onValueChange = onValueChange, placeholder = {Text(text=texto)}, modifier = Modifier.width(valor), visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation() )
    }
}

@Composable
fun MyBottomNavigation(){

    val tamanyo = 33.dp

    BottomNavigation(
        backgroundColor = Color(0xFF7d2181),
        contentColor = Color.White,
    ) {
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "home",
                modifier = Modifier.size(tamanyo)
            )
        }, label = { Text("Home", fontSize = 12.sp) })
        BottomNavigationItem(selected = false, onClick = {  }, icon = {
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