package com.jonecx.qio.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfo(
    @SerialName("meta")
    val meta: Meta,
    @SerialName("response")
    private val response: Response,
) {
    fun getProfile(): User {
        return response.user
    }
}

@Serializable
data class Response(
    val user: User,
)

@Serializable
data class Meta(
    @SerialName("status")
    val status: Int,
    @SerialName("msg")
    val msg: String,
)

@Serializable
data class User(
    @SerialName("following")
    val following: Int,
    @SerialName("default_post_format")
    val defaultPostFormat: PostFormat,
    @SerialName("name")
    val name: String,
    @SerialName("likes")
    val likes: Int,
    @SerialName("blogs")
    val blogs: List<Blog>,
)

@Serializable
data class Blog(
    @SerialName("admin")
    val isAdmin: Boolean,
    @SerialName("ask")
    val ask: Boolean,
    @SerialName("ask_anon")
    val askAnon: Boolean,
    @SerialName("ask_page_title")
    val askPageTitle: String,
    @SerialName("asks_allow_media")
    val asksAllowMedia: Boolean,
    @SerialName("avatar")
    val avatar: List<Avatar>,
    @SerialName("can_chat")
    val canChat: Boolean,
    @SerialName("can_send_fan_mail")
    val canSendFanMail: Boolean,
    @SerialName("can_subscribe")
    val canSubscribe: Boolean,
    @SerialName("description")
    val description: String,
    @SerialName("drafts")
    val drafts: Int,
    @SerialName("facebook")
    val facebook: YesNo,
    @SerialName("facebook_opengraph_enabled")
    val facebookOpengraphEnabled: YesNo,
    @SerialName("followed")
    val followed: Boolean,
    @SerialName("followers")
    val followers: Int,
    @SerialName("is_blocked_from_primary")
    val isBlockedFromPrimary: Boolean,
    @SerialName("is_nsfw")
    val isNsfw: Boolean,
    @SerialName("messages")
    val messages: Int,
    @SerialName("name")
    val name: String,
    @SerialName("posts")
    val posts: Int,
    @SerialName("primary")
    val primary: Boolean,
    @SerialName("queue")
    val queue: Int,
    @SerialName("share_likes")
    val hasShareLikes: Boolean,
    @SerialName("subscribed")
    val subscribed: Boolean,
    @SerialName("theme")
    val theme: Theme,
    @SerialName("title")
    val title: String,
    @SerialName("total_posts")
    val totalPosts: Int,
    @SerialName("tweet")
    val tweet: YesNo,
    @SerialName("twitter_enabled")
    val isTwitterEnabled: Boolean,
    @SerialName("twitter_send")
    val canTwitterSend: Boolean,
    @SerialName("type")
    val privacyType: Privacy,
    @SerialName("updated")
    val updated: Int,
    @SerialName("url")
    val url: String,
    @SerialName("uuid")
    val uuid: String,
)

@Serializable
data class Avatar(
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("url")
    val url: String,
    @SerialName("accessories")
    val accessories: List<String>,
)

@Serializable
data class Theme(
    @SerialName("header_full_width")
    val headerFullWidth: Int,
    @SerialName("header_full_height")
    val headerFullHeight: Int,
    @SerialName("avatar_shape")
    val avatarShape: AvatarShape,
    @SerialName("background_color")
    val backgroundColor: String,
    @SerialName("body_font")
    val bodyFont: String,
    @SerialName("header_bounds")
    val headerBounds: String,
    @SerialName("header_image")
    val headerImage: String,
    @SerialName("header_image_focused")
    val headerImageFocused: String,
    @SerialName("header_image_poster")
    val headerImagePoster: String,
    @SerialName("header_image_scaled")
    val headerImageScaled: String,
    @SerialName("header_stretch")
    val isHeaderStretched: Boolean,
    @SerialName("link_color")
    val linkColor: String,
    @SerialName("show_avatar")
    val isShowAvatar: Boolean,
    @SerialName("show_description")
    val isShowDescription: Boolean,
    @SerialName("show_header_image")
    val isShowHeaderImage: Boolean,
    @SerialName("show_title")
    val isShowTitle: Boolean,
    @SerialName("title_color")
    val titleColor: String,
    @SerialName("title_font")
    val titleFont: String,
    @SerialName("title_font_weight")
    val titleFontWeight: String,
)

@Serializable
enum class PostFormat {
    @SerialName("html")
    HTML,

    @SerialName("markdown")
    MARKDOWN,
}

@Serializable
enum class YesNo {
    @SerialName("N")
    N,

    @SerialName("Y")
    Y,
}

@Serializable
enum class AvatarShape {
    @SerialName("square")
    SQUARE,

    @SerialName("circle")
    CIRCLE,
}

@Serializable
enum class Privacy {
    @SerialName("public")
    PUBLIC,

    @SerialName("private")
    PRIVATE,
}
