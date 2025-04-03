import 'package:flutter/material.dart';
import 'package:picker_frontend/models/reply.dart';
import 'package:picker_frontend/models/vote.dart';

class Post {
  String user;
  Vote vote;
  int likes;
  List<Reply> replies;

  Post({
    required this.user,
    required this.vote,
    required this.likes,
    required this.replies,
  });
}

class PostData with ChangeNotifier {
  final List<Post> _posts = [
    Post(user: '1번', vote: Vote(), likes: 1, replies: List<Reply>.empty()),
    Post(user: '2번', vote: Vote(), likes: 2, replies: List<Reply>.empty()),
    Post(user: '3번', vote: Vote(), likes: 3, replies: List<Reply>.empty()),
    Post(user: '4번', vote: Vote(), likes: 4, replies: List<Reply>.empty()),
    Post(user: '5번', vote: Vote(), likes: 5, replies: List<Reply>.empty()),
  ];
  List<Post> get posts => _posts;

  void load() {
    // TODO: Post 데이터 GET
    notifyListeners();
  }

  void addPost(Post post) {
    // TODO: Post 데이터 POST
    _posts.add(post);
    notifyListeners();
  }
}
