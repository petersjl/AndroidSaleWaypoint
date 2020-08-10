package edu.rosehulman.andersc7.androidsalewaypoint

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.andersc7.androidsalewaypoint.ui.AddFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.SignInFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.Game
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameAdapter
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.sales.SalesFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.stores.StoresFragment
import edu.rosehulman.andersc7.androidsalewaypoint.ui.wishlist.WishlistFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), GameAdapter.OnGameSelectedListener, NavigationView.OnNavigationItemSelectedListener {

	private lateinit var appBarConfiguration: AppBarConfiguration

	private var auth = FirebaseAuth.getInstance()
	private lateinit var authStateListener: FirebaseAuth.AuthStateListener
	private lateinit var drawerLayout: DrawerLayout
	private lateinit var drawerToggle: ActionBarDrawerToggle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setUpToolbar()
		initializeListeners()
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
			R.id.nav_add -> {
				switchTo = AddFragment()
				toolbar.title = "Add a game"
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