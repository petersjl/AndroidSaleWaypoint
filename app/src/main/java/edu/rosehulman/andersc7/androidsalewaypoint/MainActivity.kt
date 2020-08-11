package edu.rosehulman.andersc7.androidsalewaypoint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.andersc7.androidsalewaypoint.ui.SignInFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.Game
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameAdapter
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.sales.SalesFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.stores.StoresFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.wishlist.WishlistFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_add.view.*

class MainActivity : AppCompatActivity(), GameAdapter.OnGameSelectedListener, NavigationView.OnNavigationItemSelectedListener {

	private lateinit var appBarConfiguration: AppBarConfiguration

	private var auth = FirebaseAuth.getInstance()
	private var gamesRef = FirebaseFirestore.getInstance().collection("Games")
	private lateinit var authStateListener: FirebaseAuth.AuthStateListener
	private lateinit var drawerLayout: DrawerLayout
	private lateinit var drawerToggle: ActionBarDrawerToggle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		fab.setOnClickListener { view ->
			showAddDialog()
		}
		setUpToolbar()
		initializeListeners()
	}

	private fun showAddDialog(){
		val builder = AlertDialog.Builder(this)

		//Set options
		builder.setTitle("Add a game")
		val view = LayoutInflater.from(this).inflate(R.layout.dialog_add, null, false)
		builder.setView(view)

		builder.setPositiveButton("Add"){_, _ ->
			val data = hashMapOf(
				"title" to view.add_title_text.text.toString(),
				"developer" to view.add_developer_text.text.toString(),
				"description" to view.add_description_text.text.toString()
			)
			gamesRef.add(data).addOnSuccessListener {doc: DocumentReference? ->
				val listings = doc?.collection("Listings")

				if(view.add_enabled_steam.isChecked) {
					listings?.document("steam")?.set(
						hashMapOf(
							"store" to "STEAM",
							"price" to view.add_price_steam.text.toString().toDouble(),
							"sale" to (view.add_sale_steam.text.toString().toInt() / 100.0)
						)
					)
				}
				if(view.add_enabled_playstation.isChecked) {
					listings?.document("playstation")?.set(
						hashMapOf(
							"store" to "PLAYSTATION",
							"price" to view.add_price_playstation.text.toString().toDouble(),
							"sale" to (view.add_price_playstation.text.toString().toInt() / 100.0)
						)
					)
				}
				if(view.add_enabled_xbox.isChecked) {
					listings?.document("xbox")?.set(
						hashMapOf(
							"store" to "XBOX",
							"price" to view.add_price_xbox.text.toString().toDouble(),
							"sale" to (view.add_sale_xbox.text.toString().toInt() / 100.0)
						)
					)
				}
				if(view.add_enabled_nintendo.isChecked) {
					listings?.document("nintendo")?.set(
						hashMapOf(
							"store" to "NINTENDO",
							"price" to view.add_price_nintendo.text.toString().toDouble(),
							"sale" to (view.add_price_nintendo.text.toString().toInt() / 100.0)
						)
					)
				}
				if(view.add_enabled_itch.isChecked) {
					listings?.document("itch")?.set(
						hashMapOf(
							"store" to "ITCH",
							"price" to view.add_price_itch.text.toString().toDouble(),
							"sale" to (view.add_sale_itch.text.toString().toInt() / 100.0)
						)
					)
				}

			}

		}

		builder.setNegativeButton(android.R.string.cancel, null)

		builder.show()
	}

	private fun setUpToolbar(){
		val toolbar: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(toolbar)

		drawerLayout = findViewById(R.id.drawer_layout)
		val navView: NavigationView = findViewById(R.id.nav_view)

		drawerToggle = ActionBarDrawerToggle(
			this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
		)
		drawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()
		navView.setNavigationItemSelectedListener(this)
	}

	override fun onStart() {
		super.onStart()
		auth.addAuthStateListener(authStateListener)
	}

	override fun onStop() {
		super.onStop()
		auth.removeAuthStateListener(authStateListener)
	}

	fun initializeListeners(){
		Log.d("tag", "Initializing listeners")
		authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
			val user = auth.currentUser
			Log.d("tag", "User is: $user")
			if (user == null){
				supportActionBar?.hide()

				val ft = supportFragmentManager.beginTransaction()
				ft.replace(R.id.fragment_container, SignInFragment())
				ft.commit()
			}
			else {
				toolbar.title = "Wishlist"
				supportActionBar?.show()
				val ft = supportFragmentManager.beginTransaction()
				ft.replace(R.id.fragment_container, WishlistFragment())
				ft.commit()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	override fun onGameSelected(game: Game) {
		Log.d(Constants.TAG, "Game selected: ${game.title}")
		val gameFragment = GameFragment.newInstance(game)
		val ft = this.supportFragmentManager.beginTransaction()
		ft.replace(R.id.fragment_container, gameFragment)
		ft.addToBackStack("game")
		ft.commit()
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		var switchTo: Fragment? = null
		// Handle navigation view item clicks here.
		when (item.itemId) {
			R.id.nav_wishlist -> {
				switchTo = WishlistFragment()
				toolbar.title = "Wishlist"
			}
			R.id.nav_sales -> {
				switchTo = SalesFragment()
				toolbar.title = "What's on sale"
			}
			R.id.nav_log_out ->
				auth.signOut()
			R.id.nav_steam -> {
				switchTo = StoresFragment(Constants.Steam)
				toolbar.title = "Steam"
			}
			R.id.nav_playstation -> {
				switchTo = StoresFragment(Constants.PlayStation)
				toolbar.title = "PlayStation"
			}
			R.id.nav_xbox -> {
				switchTo = StoresFragment(Constants.Xbox)
				toolbar.title = "Xbox"
			}
			R.id.nav_nintendo -> {
				switchTo = StoresFragment(Constants.Nintendo)
				toolbar.title = "Nintendo"
			}
			R.id.nav_itch -> {
				switchTo = StoresFragment(Constants.Itch)
				toolbar.title = "Itch"
			}
		}
		if (switchTo != null) {
			val ft = supportFragmentManager.beginTransaction()
			ft.replace(R.id.fragment_container, switchTo)
			while (supportFragmentManager.backStackEntryCount > 0){
				supportFragmentManager.popBackStackImmediate()
			}
			ft.commit()
		}
		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}
}