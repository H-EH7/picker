import 'package:flutter/material.dart';
import 'package:picker_frontend/models/post.dart';

class PostItem extends StatelessWidget {
  final Post post;
  const PostItem({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    return Text("${post.user}");
  }
}
