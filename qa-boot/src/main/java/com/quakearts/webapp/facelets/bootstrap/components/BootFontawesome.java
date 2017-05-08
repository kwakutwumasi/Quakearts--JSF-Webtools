/*******************************************************************************
 * Copyright (C) 2017 Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com> - initial API and implementation
 ******************************************************************************/
package com.quakearts.webapp.facelets.bootstrap.components;

import java.util.HashMap;

import javax.faces.component.UIOutput;

import com.quakearts.webapp.facelets.util.ObjectExtractor;

public class BootFontawesome extends UIOutput {
	private static final HashMap<String, String> VALIDVALUESMAP = new HashMap<String, String>();
	private static final String EMPTY = "";
	private static final String[] VALIDVALUES = new String[] { "500px", "address-book-o", "address-book",
			"address-card-o", "address-card", "adjust", "adn", "align-center", "align-justify", "align-left",
			"align-right", "amazon", "ambulance", "american-sign-language-interpreting", "anchor", "android",
			"angellist", "angle-double-down", "angle-double-left", "angle-double-right", "angle-double-up",
			"angle-down", "angle-left", "angle-right", "angle-up", "apple", "archive", "area-chart",
			"arrow-circle-down", "arrow-circle-left", "arrow-circle-o-down", "arrow-circle-o-left",
			"arrow-circle-o-right", "arrow-circle-o-up", "arrow-circle-right", "arrow-circle-up", "arrow-down",
			"arrow-left", "arrow-right", "arrow-up", "arrows-alt", "arrows-h", "arrows-v", "arrows", "asl-interpreting",
			"assistive-listening-systems", "asterisk", "at", "audio-description", "automobile", "backward",
			"balance-scale", "ban", "bandcamp", "bank", "bar-chart-o", "bar-chart", "barcode", "bars", "bath",
			"bathtub", "battery-0", "battery-1", "battery-2", "battery-3", "battery-4", "battery-empty", "battery-full",
			"battery-half", "battery-quarter", "battery-three-quarters", "battery", "bed", "beer", "behance-square",
			"behance", "bell-o", "bell-slash-o", "bell-slash", "bell", "bicycle", "binoculars", "birthday-cake",
			"bitbucket-square", "bitbucket", "bitcoin", "black-tie", "blind", "bluetooth-b", "bluetooth", "bold",
			"bolt", "bomb", "book", "bookmark-o", "bookmark", "braille", "briefcase", "btc", "bug", "building-o",
			"building", "bullhorn", "bullseye", "bus", "buysellads", "cab", "calculator", "calendar-check-o",
			"calendar-minus-o", "calendar-o", "calendar-plus-o", "calendar-times-o", "calendar", "camera-retro",
			"camera", "car", "caret-down", "caret-left", "caret-right", "caret-square-o-down", "caret-square-o-left",
			"caret-square-o-right", "caret-square-o-up", "caret-up", "cart-arrow-down", "cart-plus", "cc-amex",
			"cc-diners-club", "cc-discover", "cc-jcb", "cc-mastercard", "cc-paypal", "cc-stripe", "cc-visa", "cc",
			"certificate", "chain-broken", "chain", "check-circle-o", "check-circle", "check-square-o", "check-square",
			"check", "chevron-circle-down", "chevron-circle-left", "chevron-circle-right", "chevron-circle-up",
			"chevron-down", "chevron-left", "chevron-right", "chevron-up", "child", "chrome", "circle-o-notch",
			"circle-o", "circle-thin", "circle", "clipboard", "clock-o", "clone", "close", "cloud-download",
			"cloud-upload", "cloud", "cny", "code-fork", "code", "codepen", "codiepie", "coffee", "cog", "cogs",
			"columns", "comment-o", "comment", "commenting-o", "commenting", "comments-o", "comments", "compass",
			"compress", "connectdevelop", "contao", "copy", "copyright", "creative-commons", "credit-card-alt",
			"credit-card", "crop", "crosshairs", "css3", "cube", "cubes", "cut", "cutlery", "dashboard", "dashcube",
			"database", "deaf", "deafness", "dedent", "delicious", "desktop", "deviantart", "diamond", "digg", "dollar",
			"dot-circle-o", "download", "dribbble", "drivers-license-o", "drivers-license", "dropbox", "drupal", "edge",
			"edit", "eercast", "eject", "ellipsis-h", "ellipsis-v", "empire", "envelope-o", "envelope-open-o",
			"envelope-open", "envelope-square", "envelope", "envira", "eraser", "etsy", "eur", "euro", "exchange",
			"exclamation-circle", "exclamation-triangle", "exclamation", "expand", "expeditedssl",
			"external-link-square", "external-link", "eye-slash", "eye", "eyedropper", "fa", "facebook-f",
			"facebook-official", "facebook-square", "facebook", "fast-backward", "fast-forward", "fax", "feed",
			"female", "fighter-jet", "file-archive-o", "file-audio-o", "file-code-o", "file-excel-o", "file-image-o",
			"file-movie-o", "file-o", "file-pdf-o", "file-photo-o", "file-picture-o", "file-powerpoint-o",
			"file-sound-o", "file-text-o", "file-text", "file-video-o", "file-word-o", "file-zip-o", "file", "files-o",
			"film", "filter", "fire-extinguisher", "fire", "firefox", "first-order", "flag-checkered", "flag-o", "flag",
			"flash", "flask", "flickr", "floppy-o", "folder-o", "folder-open-o", "folder-open", "folder",
			"font-awesome", "font", "fonticons", "fort-awesome", "forumbee", "forward", "foursquare", "free-code-camp",
			"frown-o", "futbol-o", "gamepad", "gavel", "gbp", "ge", "gear", "gears", "genderless", "get-pocket",
			"gg-circle", "gg", "gift", "git-square", "git", "github-alt", "github-square", "github", "gitlab", "gittip",
			"glass", "glide-g", "glide", "globe", "google-plus-circle", "google-plus-official", "google-plus-square",
			"google-plus", "google-wallet", "google", "graduation-cap", "gratipay", "grav", "group", "h-square",
			"hacker-news", "hand-grab-o", "hand-lizard-o", "hand-o-down", "hand-o-left", "hand-o-right", "hand-o-up",
			"hand-paper-o", "hand-peace-o", "hand-pointer-o", "hand-rock-o", "hand-scissors-o", "hand-spock-o",
			"hand-stop-o", "handshake-o", "hard-of-hearing", "hashtag", "hdd-o", "header", "headphones", "heart-o",
			"heart", "heartbeat", "history", "home", "hospital-o", "hotel", "hourglass-1", "hourglass-2", "hourglass-3",
			"hourglass-end", "hourglass-half", "hourglass-o", "hourglass-start", "hourglass", "houzz", "html5",
			"i-cursor", "id-badge", "id-card-o", "id-card", "ils", "image", "imdb", "inbox", "indent", "industry",
			"info-circle", "info", "inr", "instagram", "institution", "internet-explorer", "intersex", "ioxhost",
			"italic", "joomla", "jpy", "jsfiddle", "key", "keyboard-o", "krw", "language", "laptop", "lastfm-square",
			"lastfm", "leaf", "leanpub", "legal", "lemon-o", "level-down", "level-up", "life-bouy", "life-buoy",
			"life-ring", "life-saver", "lightbulb-o", "line-chart", "link", "linkedin-square", "linkedin", "linode",
			"linux", "list-alt", "list-ol", "list-ul", "list", "location-arrow", "lock", "long-arrow-down",
			"long-arrow-left", "long-arrow-right", "long-arrow-up", "low-vision", "magic", "magnet", "mail-forward",
			"mail-reply-all", "mail-reply", "male", "map-marker", "map-o", "map-pin", "map-signs", "map", "mars-double",
			"mars-stroke-h", "mars-stroke-v", "mars-stroke", "mars", "maxcdn", "meanpath", "medium", "medkit", "meetup",
			"meh-o", "mercury", "microchip", "microphone-slash", "microphone", "minus-circle", "minus-square-o",
			"minus-square", "minus", "mixcloud", "mobile-phone", "mobile", "modx", "money", "moon-o", "mortar-board",
			"motorcycle", "mouse-pointer", "music", "navicon", "neuter", "newspaper-o", "object-group",
			"object-ungroup", "odnoklassniki-square", "odnoklassniki", "opencart", "openid", "opera", "optin-monster",
			"outdent", "pagelines", "paint-brush", "paper-plane-o", "paper-plane", "paperclip", "paragraph", "paste",
			"pause-circle-o", "pause-circle", "pause", "paw", "paypal", "pencil-square-o", "pencil-square", "pencil",
			"percent", "phone-square", "phone", "photo", "picture-o", "pie-chart", "pied-piper-alt", "pied-piper-pp",
			"pied-piper", "pinterest-p", "pinterest-square", "pinterest", "plane", "play-circle-o", "play-circle",
			"play", "plug", "plus-circle", "plus-square-o", "plus-square", "plus", "podcast", "power-off", "print",
			"product-hunt", "puzzle-piece", "qq", "qrcode", "question-circle-o", "question-circle", "question", "quora",
			"quote-left", "quote-right", "ra", "random", "ravelry", "rebel", "recycle", "reddit-alien", "reddit-square",
			"reddit", "refresh", "registered", "remove", "renren", "reorder", "repeat", "reply-all", "reply",
			"resistance", "retweet", "rmb", "road", "rocket", "rotate-left", "rotate-right", "rouble", "rss-square",
			"rss", "rub", "ruble", "rupee", "s15", "safari", "save", "scissors", "scribd", "search-minus",
			"search-plus", "search", "sellsy", "send-o", "send", "server", "share-alt-square", "share-alt",
			"share-square-o", "share-square", "share", "shekel", "sheqel", "shield", "ship", "shirtsinbulk",
			"shopping-bag", "shopping-basket", "shopping-cart", "shower", "sign-in", "sign-language", "sign-out",
			"signal", "signing", "simplybuilt", "sitemap", "skyatlas", "skype", "slack", "sliders", "slideshare",
			"smile-o", "snapchat-ghost", "snapchat-square", "snapchat", "snowflake-o", "soccer-ball-o",
			"sort-alpha-asc", "sort-alpha-desc", "sort-amount-asc", "sort-amount-desc", "sort-asc", "sort-desc",
			"sort-down", "sort-numeric-asc", "sort-numeric-desc", "sort-up", "sort", "soundcloud", "space-shuttle",
			"spinner", "spoon", "spotify", "square-o", "square", "stack-exchange", "stack-overflow", "star-half-empty",
			"star-half-full", "star-half-o", "star-half", "star-o", "star", "steam-square", "steam", "step-backward",
			"step-forward", "stethoscope", "sticky-note-o", "sticky-note", "stop-circle-o", "stop-circle", "stop",
			"street-view", "strikethrough", "stumbleupon-circle", "stumbleupon", "subscript", "subway", "suitcase",
			"sun-o", "superpowers", "superscript", "support", "table", "tablet", "tachometer", "tag", "tags", "tasks",
			"taxi", "telegram", "television", "tencent-weibo", "terminal", "text-height", "text-width", "th-large",
			"th-list", "th", "themeisle", "thermometer-0", "thermometer-1", "thermometer-2", "thermometer-3",
			"thermometer-4", "thermometer-empty", "thermometer-full", "thermometer-half", "thermometer-quarter",
			"thermometer-three-quarters", "thermometer", "thumb-tack", "thumbs-down", "thumbs-o-down", "thumbs-o-up",
			"thumbs-up", "ticket", "times-circle-o", "times-circle", "times-rectangle-o", "times-rectangle", "times",
			"tint", "toggle-down", "toggle-left", "toggle-off", "toggle-on", "toggle-right", "toggle-up", "trademark",
			"train", "transgender-alt", "transgender", "trash-o", "trash", "tree", "trello", "tripadvisor", "trophy",
			"truck", "try", "tty", "tumblr-square", "tumblr", "turkish-lira", "tv", "twitch", "twitter-square",
			"twitter", "umbrella", "underline", "undo", "universal-access", "university", "unlink", "unlock-alt",
			"unlock", "unsorted", "upload", "usb", "usd", "user-circle-o", "user-circle", "user-md", "user-o",
			"user-plus", "user-secret", "user-times", "user", "users", "vcard-o", "vcard", "venus-double", "venus-mars",
			"venus", "viacoin", "viadeo-square", "viadeo", "video-camera", "vimeo-square", "vimeo", "vine", "vk",
			"volume-control-phone", "volume-down", "volume-off", "volume-up", "warning", "wechat", "weibo", "weixin",
			"whatsapp", "wheelchair-alt", "wheelchair", "wifi", "wikipedia-w", "window-close-o", "window-close",
			"window-maximize", "window-minimize", "window-restore", "windows", "won", "wordpress", "wpbeginner",
			"wpexplorer", "wpforms", "wrench", "xing-square", "xing", "y-combinator-square", "y-combinator", "yahoo",
			"yc-square", "yc", "yelp", "yen", "yoast", "youtube-play", "youtube-square", "youtube"};
	
	static {
		for(String validValue:VALIDVALUES){
			VALIDVALUESMAP.put(validValue, EMPTY);
		}
	}
	
	public static final String COMPONENT_FAMILY="com.quakearts.bootstrap.fontawesome";
	public static final String RENDERER_TYPE="com.quakearts.bootstrap.fontawesome.renderer";

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public boolean isValid(String type){
		return VALIDVALUESMAP.get(type)!=null;
	}

	@Override
	public String getRendererType() {
		return RENDERER_TYPE;
	}

	@Override
	public void setRendererType(String rendererType) {
	}
	
	public String get(String attribute) {
		String attributeValue = ObjectExtractor
				.extractString(getValueExpression(attribute), getFacesContext()
						.getELContext());
		if (attributeValue == null)
			attributeValue = (String) getAttributes().get(attribute);

		return attributeValue;
	}
}
