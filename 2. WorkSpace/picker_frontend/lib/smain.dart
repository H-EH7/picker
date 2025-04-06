import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:picker_frontend/screens/search/search_screen.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create:(context)=> MyAppState(),
      child: MaterialApp(
        home: const Search(),
      ),
    );
  }
}

class Search extends StatefulWidget {
  const Search({super.key});

  @override
  State<Search> createState() => _SearchState();
}

class _SearchState extends State<Search> {
  final FocusNode _focusNode = FocusNode();
  bool isFocused = false;
  @override
  void initState() {
    super.initState();
    _focusNode.addListener(() {
      setState(() {
        isFocused = _focusNode.hasFocus;
      });
    });
  }

  @override
  void dispose() {
    _focusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: null,
      body: GestureDetector(
        onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
        child: Column(
          children: [
            SizedBox(height: 20),
            TextField(
              focusNode: _focusNode,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.search),
                labelText: "검색",
              ),
            ),
            Expanded(
              child: AnimatedContainer(
                duration: Duration(milliseconds: 300),
                color: isFocused ? Colors.green : Colors.red,
                child: Center(
                  child: isFocused? UserList():SearchList(),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class UserList extends StatefulWidget{
  const UserList({super.key});

  @override
  State<UserList> createState() =>_UserList();
}

class _UserList extends State<UserList> {

  @override
  Widget build(BuildContext context) {
    var appState = context.watch<MyAppState>();
    var userlist = appState._UserList;

    return Center(
      child: Column(
        children: [

          for(var user in userlist)
            UserProfile(user: user),
          // ListView.builder(
          //   itemCount: userlist.length,
          //   itemBuilder: (context, index){
          //     final user = userlist[index];
          //     return Text(
          //         user.name
          //       );
          //   }
          //   )
          
        ],
      ),
    );
  }
  
}

class UserProfile extends StatelessWidget {
  const UserProfile({
    super.key,
    required this.user,
  });

  final User user;

  @override
    Widget build(BuildContext context) {
    // return  Text(user.name);
       return SizedBox(
      width:double.infinity,
      child: Text(user.name)
      );
  }
} 
class SearchList extends StatefulWidget{
  const SearchList({super.key});

  @override
  State<SearchList> createState() =>_SearchList();
}

class _SearchList extends State<SearchList> {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        children: [
          Text("_SearchList"),
        ],
      ),
    );
  }
  
}
class MyAppState extends ChangeNotifier{
  final List<User> _UserList= [
    User(name: "john",id: "john123"),
    User(name: "john",id: "johongood"),
    User(name: "john",id: "johongood12"),
  ];

}

 
class User {
  final String name;
  final String id;

  User({required this.name, required this.id});
}