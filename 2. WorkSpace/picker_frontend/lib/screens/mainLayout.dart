import 'package:flutter/material.dart';
import 'export_screen.dart';
import '../services/export_state.dart';
import 'package:provider/provider.dart';


class Mainlayout extends StatefulWidget {
  const Mainlayout({super.key});

  @override
  State<Mainlayout> createState() => _MainlayoutState();
}

class _MainlayoutState extends State<Mainlayout> {
  int _selectedIndex = 0;

  final List<Widget> _screens = [
    ChangeNotifierProvider(
      create: (context) => FeedState(),
      child: FeedScreen(),    
    ),
    ChangeNotifierProvider(
      create: (context) => PickingState(),
      child: PickingScreen(),    
    ),
    ChangeNotifierProvider(
      create: (context) => SearchState(),
      child: SearchScreen(),    
    ),
    ChangeNotifierProvider(
      create: (context) => CommonState(),
      child: MyPage(),    
    ),
  ];

  void _onItemTapped(int index){
    setState(() {
      _selectedIndex = index;
    });
  }
  @override
  Widget build(BuildContext context) {
        return Scaffold(
      body: _screens[_selectedIndex],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _selectedIndex,
        onTap: _onItemTapped,
        items: [
          BottomNavigationBarItem(icon: Icon(Icons.home),label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.search,),label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.check_circle,),label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.account_circle,),label: ''),
        ],
        selectedItemColor: Colors.grey,
        unselectedItemColor: Colors.black,
        type: BottomNavigationBarType.fixed,
        showSelectedLabels: false,
        showUnselectedLabels: false,
        selectedIconTheme: const IconThemeData(size: 24),
        unselectedIconTheme: const IconThemeData(size: 24),
      ),
    );
  }
}