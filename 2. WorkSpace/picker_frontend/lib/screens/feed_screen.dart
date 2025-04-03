import 'package:flutter/material.dart';
import 'package:picker_frontend/models/post.dart';
import 'package:picker_frontend/widgets/post_item.dart';
import 'package:provider/provider.dart';

class FeedScreen extends StatelessWidget {
  const FeedScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<PostData>(
      builder: (context, postData, child) {
        return ListView.builder(
          itemCount: postData.posts.length,
          itemBuilder: (context, index) {
            return PostItem(post: postData.posts[index]);
          },
        );
      },
    );
  }
}
