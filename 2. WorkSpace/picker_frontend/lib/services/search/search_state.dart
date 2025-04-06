import 'package:flutter/material.dart';

class SearchState extends ChangeNotifier{
  // final List<User> _UserList= [
  //   User(name: "john",id: "john123"),
  //   User(name: "john",id: "johongood"),
  //   User(name: "john",id: "johongood12"),
  // ];

}
 
// class UserList extends StatefulWidget{
//   const UserList({super.key});

//   @override
//   State<UserList> createState() =>_UserList();
// }

// class _UserList extends State<UserList> {

//   @override
//   Widget build(BuildContext context) {
//     var appState = context.watch<SearchState>();
//     var userlist = appState._UserList;

//     return Center(
//       child: Column(
//         children: [

//           for(var user in userlist)
//             UserProfile(user: user),
//           // ListView.builder(
//           //   itemCount: userlist.length,
//           //   itemBuilder: (context, index){
//           //     final user = userlist[index];
//           //     return Text(
//           //         user.name
//           //       );
//           //   }
//           //   )
          
//         ],
//       ),
//     );
//   }
  
// }


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

class User {
  final String name;
  final String id;

  User({required this.name, required this.id});
}