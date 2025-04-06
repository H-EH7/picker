import 'package:flutter/material.dart';
import 'package:picker_frontend/models/common/user.dart';


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