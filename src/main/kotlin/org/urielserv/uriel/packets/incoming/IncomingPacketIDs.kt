package org.urielserv.uriel.packets.incoming

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

@Suppress("unused", "ConstPropertyName")
object IncomingPacketIDs {
    
    const val Pong = 2596
    const val ChangeNameCheckUsername = 3950
    const val ConfirmChangeName = 2977
    const val ActivateEffect = 2959
    const val EnableEffect = 1752
    const val UserActivity = 3457
    const val NavigatorCategoryListMode = 1202
    const val NavigatorCollapseCategory = 1834
    const val NavigatorUncollapseCategory = 637
    const val PickNewUserGift = 1822
    const val FootballGateSaveLook = 924
    const val MannequinSaveLook = 2209
    const val RequestCatalogPage = 412
    const val RequestWearingBadges = 2091
    const val BotPickup = 3323
    const val HorseRide = 1036
    const val RequestCreateRoom = 2752
    const val SaveMotto = 2228
    const val ModToolAlert = 1840
    const val TradeAccept = 3863
    const val RequestCatalogMode = 1195
    const val RequestUserCredits = 273
    const val FriendPrivateMessage = 3567
    const val CloseDice = 1533
    const val RoomUserRemoveRights = 2064
    const val RoomRemoveRights = 3182
    const val GuildDeclineMembership = 1894
    const val AnswerPoll = 3505
    const val UserWearBadge = 644
    const val RoomVote = 3582
    const val RoomUserSign = 1975
    const val RequestUserData = 357
    const val RoomUserShout = 2085
    const val ScratchPet = 3202
    const val RoomUserWalk = 3320
    const val RequestTags = 826
    const val GetMarketplaceConfig = 2597
    const val RequestHeightmap = 3898
    const val TradeClose = 2551
    const val CatalogBuyItem = 3492
    const val CatalogSelectClubGift = 2276
    const val CatalogRequestClubDiscount = 2462
    const val CatalogBuyClubDiscount = 3407
    const val RequestGuildMembers = 312
    const val RequestPetInformation = 2934
    const val RoomUserWhisper = 1543
    const val ModToolRequestUserInfo = 3295
    const val RotateMoveItem = 248
    const val CancelPoll = 1773
    const val RequestRoomLoad = 2312
    const val RequestGuildParts = 813
    const val RoomPlacePaint = 711
    const val RequestPopularRooms = 2758
    const val ModToolRequestRoomInfo = 707
    const val FriendRequest = 3157
    const val Recycle = 2771
    const val RequestRoomCategories = 3027 //1371;

    const val ToggleWallItem = 210
    const val RoomUserTalk = 1314
    const val HotelViewData = 2912
    const val RoomUserDance = 2080
    const val RequestUserProfile = 3265
    const val SearchRoomsFriendsNow = 1786
    const val SetStackHelperHeight = 3839
    const val RedeemVoucher = 339
    const val PetUseItem = 1328
    const val HorseRemoveSaddle = 186
    const val BuyItem = 1603
    const val AdvertisingSave = 3608
    const val RequestPetTrainingPanel = 2161
    const val RoomBackground = 2880
    const val RequestNewsList = 1827
    const val RequestPromotedRooms = 2908
    const val GuildSetAdmin = 2894
    const val GetClubData = 3285
    const val RequestClubCenter = 869
    const val RequestMeMenuSettings = 2388
    const val MannequinSaveName = 2850
    const val SellItem = 3447
    const val GuildAcceptMembership = 3386
    const val RequestRecylerLogic = 398
    const val RequestGuildJoin = 998
    const val RequestCatalogIndex = 2529
    const val RequestInventoryPets = 3095
    const val ModToolRequestRoomVisits = 3526
    const val ModToolWarn = -1 //3763

    const val RequestItemInfo = 3288
    const val ModToolRequestRoomChatlog = 2587
    const val UserSaveLook = 2730
    const val ToggleFloorItem = 99
    const val TradeUnAccept = 1444
    const val WiredTriggerSaveData = 1520
    const val RoomRemoveAllRights = 2683
    const val TakeBackItem = 434
    const val OpenRecycleBox = 3558
    const val GuildChangeNameDesc = 3137
    const val RequestSellItem = 848
    const val ModToolChangeRoomSettings = 3260
    const val ModToolRequestUserChatlog = 1391 //203

    const val GuildChangeSettings = 3435
    const val RoomUserDropHandItem = 2814
    const val RequestProfileFriends = 2138
    const val TradeCancelOfferItem = 3845
    const val TriggerDice = 1990
    const val GetPollData = 109
    const val MachineID = 2490
    const val RequestDiscount = 223
    const val RequestFriendRequest = 2448
    const val RoomSettingsSave = 1969
    const val AcceptFriendRequest = 137
    const val DeclineFriendRequest = 2890 //835

    const val ReleaseVersion = 4000 //4000 

    const val InitDiffieHandshake = 3110
    const val CompleteDiffieHandshake = 773
    const val SearchRoomsMyFavorite = 2578
    const val TradeStart = 1481
    const val RequestTargetOffer = 2487
    const val ChangeRelation = 3768
    const val RoomUserSit = 2235
    const val RequestCanCreateRoom = 2128
    const val ModToolKick = 2582
    const val MoveWallItem = 168
    const val SearchRooms = 3943
    const val RequestHighestScoreRooms = 2939
    const val CatalogBuyItemAsGift = 1411
    const val RoomUserGiveRespect = 2694
    const val RemoveFriend = 1689
    const val SearchRoomsFriendsOwn = 2266
    const val GuildSetFavorite = 3549
    const val PetPlace = 2647
    const val BotSettings = 1986
    const val StalkFriend = 3997
    const val RoomPickupItem = 3456
    const val RedeemItem = 3115
    const val RequestFriends = 1523
    const val RequestAchievements = 219
    const val GuildChangeColors = 1764
    const val RequestInventoryBadges = 2769
    const val HotelViewInventory = 3500
    const val RequestPetBreeds = 1756
    const val GuildChangeBadge = 1991
    const val ModToolBan = -1
    const val SaveWardrobe = 800
    const val HotelView = 105
    const val ModToolPickTicket = 15
    const val ModToolReleaseTicket = 1572
    const val ModToolCloseTicket = 2067
    const val TriggerColorWheel = 2144
    const val SearchRoomsByTag = -1 //1956

    const val RequestPublicRooms = 1229
    const val RequestResolution = 359
    const val RequestInventoryItems = 3150
    const val ModToolRoomAlert = 3842
    const val WiredEffectSaveData = 2281
    const val WiredApplySetConditions = 3373
    const val CheckPetName = 2109
    const val SecureLogin = 2419
    const val BotSaveSettings = 2624
    const val RequestGuildBuy = 230
    const val SearchUser = 1210
    const val GuildConfirmRemoveMember = 3593
    const val GuildRemoveMember = 593
    const val WiredConditionSaveData = 3203
    const val RoomUserLookAtPoint = 3301
    const val MoodLightTurnOn = 2296
    const val MoodLightSettings = 2813
    const val RequestMyRooms = 2277
    const val RequestCredits = 2650
    const val SearchRoomsInGroup = 39
    const val HorseRideSettings = 1472
    const val HandleDoorbell = 1644
    const val RoomUserKick = 1320
    const val RoomPlaceItem = 1258
    const val RequestInventoryBots = 3848
    const val RequestUserWardrobe = 2742
    const val RequestRoomRights = 3385
    const val RequestGuildBuyRooms = 798
    const val BotPlace = 1592
    const val SearchRoomsWithRights = 272
    const val HotelViewRequestBonusRare = 957
    const val GuildRemoveAdmin = 722
    const val RequestRoomSettings = 3129
    const val RequestOffers = 2407
    const val RequestUserCitizinShip = 2127
    const val RoomUserStopTyping = 1474
    const val RoomUserStartTyping = 1597
    const val RequestGuildManage = 1004
    const val RequestUserClub = 3166
    const val PetPickup = 1581
    const val RequestOwnGuilds = 367
    const val SearchRoomsVisited = 2264
    const val TradeOfferItem = 3107
    const val TradeOfferMultipleItems = 1263
    const val TradeConfirm = 2760
    const val RoomUserGiveRights = 808
    const val RequestGuildInfo = 2991
    const val ReloadRecycler = 1342
    const val RoomUserAction = 2456
    const val RequestGiftConfiguration = 418
    const val RequestRoomData = 2230
    const val RequestRoomHeightmap = 2300
    const val RequestGuildFurniWidget = 2651
    const val RequestOwnItems = 2105
    const val RequestReportRoom = 3267
    const val Report = 1691
    const val TriggerOneWayGate = 2765
    const val FloorPlanEditorSave = 875
    const val FloorPlanEditorRequestDoorSettings = 3559
    const val FloorPlanEditorRequestBlockedTiles = 1687
    const val Unknown1 = 1371
    const val RequestTalenTrack = 196
    const val RequestNewNavigatorData = 2110
    const val RequestNewNavigatorRooms = 249
    const val RedeemClothing = 3374
    const val NewNavigatorAction = 1703
    const val PostItPlace = 2248
    const val PostItRequestData = 3964
    const val PostItSaveData = 3666
    const val PostItDelete = 3336
    const val UseRandomStateItem = 3617

    const val MySanctionStatus = 2746

    const val MoodLightSaveSettings = 1648
    const val ModToolRequestIssueChatlog = 211
    const val ModToolRequestRoomUserChatlog = -1
    const val Username = 3878
    const val RequestClubGifts = 487
    const val RentSpace = 2946
    const val RentSpaceCancel = 1667
    const val RequestInitFriends = 2781
    const val RequestCameraConfiguration = 796
    const val Ping = 295
    const val FindNewFriends = 516
    const val InviteFriends = 1276
    const val GuildRemoveFavorite = 1820
    const val GuildDelete = 1134
    const val SetHomeRoom = 1740
    const val RoomUserGiveHandItem = 2941
    const val AmbassadorVisitCommand = 2970
    const val AmbassadorAlertCommand = 2996
    const val SaveUserVolumes = 1367
    const val SavePreferOldChat = 1262
    const val SaveIgnoreRoomInvites = 1086
    const val SaveBlockCameraFollow = 1461
    const val RoomMute = 3637
    const val RequestRoomWordFilter = 1911
    const val RoomWordFilterModify = 3001
    const val RequestRoomUserTags = 17
    const val CatalogSearchedItem = 2594
    const val JukeBoxRequestTrackCode = 3189
    const val JukeBoxRequestTrackData = 3082
    const val RoomStaffPick = 1918
    const val RoomRequestBannedUsers = 2267
    const val JukeBoxRequestPlayList = 1325
    const val JukeBoxOne = 2304
    const val JukeBoxTwo = 1435
    const val RoomUserMute = 3485

    //public static final int JukeBoxThree = 3846;
    const val RequestDeleteRoom = 532
    const val RequestPromotionRooms = 1075
    const val BuyRoomPromotion = 777
    const val EditRoomPromotionMessage = 3991
    const val RequestGuideTool = 1922
    const val RequestGuideAssistance = 3338
    const val GuideUserTyping = 519
    const val GuideReportHelper = 3969
    const val GuideRecommendHelper = 477
    const val GuideUserMessage = 3899
    const val GuideCancelHelpRequest = 291
    const val GuideHandleHelpRequest = 1424
    const val GuideVisitUser = 1052
    const val GuideInviteUser = 234
    const val GuideCloseHelpRequest = 887
    const val GuardianNoUpdatesWanted = 2501
    const val GuardianVote = 3961
    const val GuardianAcceptRequest = 3365
    const val RequestAchievementConfiguration = -1
    const val RequestReportUserBullying = 3786
    const val ReportBully = 3060
    const val CameraRoomPicture = 3226
    const val CameraRoomThumbnail = 1982
    const val SavePostItStickyPole = 3283
    const val HotelViewClaimBadge = 3077
    const val HotelViewRequestCommunityGoal = 1145
    const val HotelViewRequestConcurrentUsers = 1343
    const val HotelViewConcurrentUsersButton = 3872
    const val IgnoreRoomUser = 1117
    const val UnIgnoreRoomUser = 2061
    const val UnbanRoomUser = 992
    const val RoomUserBan = 1477
    const val RequestNavigatorSettings = 1782
    const val AddSavedSearch = 2226
    const val DeleteSavedSearch = 1954
    const val SaveWindowSettings = 3159
    const val GetHabboGuildBadgesMessage = 21
    const val UpdateUIFlags = 2313
    const val ReportThread = 534
    const val ReportComment = 1412
    const val ReportPhoto = 2492

    const val RequestCraftingRecipes = 1173
    const val RequestCraftingRecipesAvailable = 3086
    const val CraftingAddRecipe = 633
    const val CraftingCraftItem = 3591
    const val CraftingCraftSecret = 1251

    const val AdventCalendarOpenDay = 2257
    const val AdventCalendarForceOpen = 3889
    const val CameraPurchase = 2408
    const val RoomFavorite = 3817
    const val RoomUnFavorite = 309

    const val YoutubeRequestPlaylists = 336
    const val YoutubeRequestStateChange = 3005
    const val YoutubeRequestPlaylistChange = 2069

    const val HotelViewRequestBadgeReward = 2318
    const val HotelViewClaimBadgeReward = -1

    const val JukeBoxAddSoundTrack = 753
    const val JukeBoxRemoveSoundTrack = 3050
    const val ToggleMonsterplantBreedable = 3379
    const val CompostMonsterplant = 3835
    const val BreedMonsterplants = 1638
    const val MovePet = 3449
    const val PetPackageName = 3698

    const val GameCenterRequestGames = 741
    const val GameCenterRequestAccountStatus = 3171
    const val GameCenterRequestGameStatus = 11
    const val CameraPublishToWeb = 2068

    const val GameCenterJoinGame = 1458
    const val GameCenterLoadGame = 1054
    const val GameCenter = 2914
    const val GameCenterLeaveGame = 3207

    const val ModToolSanctionAlert = 229
    const val ModToolSanctionMute = 1945
    const val ModToolSanctionBan = 2766
    const val ModToolSanctionTradeLock = 3742
    const val UserNux = 1299

    const val ReportFriendPrivateChat = 2950
    const val ModToolIssueChangeTopic = 1392
    const val ModToolIssueDefaultSanction = 2717

    const val TradeCancel = 2341
    const val ChangeChatBubble = 1030
    const val LoveLockStartConfirm = 3775

    const val HotelViewRequestLTDAvailability = 410
    const val HotelViewRequestSecondsUntil = 271

    const val PurchaseTargetOffer = 1826
    const val TargetOfferState = 2041
    const val StopBreeding = 2713
    const val ConfirmPetBreeding = 3382


    const val GuildForumList = 873
    const val GuildForumThreads = 436
    const val GuildForumData = 3149
    const val GuildForumPostThread = 3529
    const val GuildForumUpdateSettings = 2214
    const val GuildForumThreadsMessages = 232
    const val GuildForumModerateMessage = 286
    const val GuildForumModerateThread = 1397
    const val GuildForumThreadUpdate = 3045
    const val GuildForumMarkAsRead = 1855


    const val UNKNOWN_SNOWSTORM_6000 = 6000
    const val UNKNOWN_SNOWSTORM_6001 = 6001
    const val UNKNOWN_SNOWSTORM_6002 = 6002
    const val UNKNOWN_SNOWSTORM_6003 = 6003
    const val UNKNOWN_SNOWSTORM_6004 = 6004
    const val UNKNOWN_SNOWSTORM_6005 = 6005
    const val UNKNOWN_SNOWSTORM_6006 = 6006
    const val UNKNOWN_SNOWSTORM_6007 = 6007
    const val UNKNOWN_SNOWSTORM_6008 = 6008
    const val UNKNOWN_SNOWSTORM_6009 = 6009
    const val UNKNOWN_SNOWSTORM_6010 = 6010
    const val UNKNOWN_SNOWSTORM_6011 = 6011
    const val SnowStormJoinQueue = 6012
    const val UNKNOWN_SNOWSTORM_6013 = 6013
    const val UNKNOWN_SNOWSTORM_6014 = 6014
    const val UNKNOWN_SNOWSTORM_6015 = 6015
    const val UNKNOWN_SNOWSTORM_6016 = 6016
    const val UNKNOWN_SNOWSTORM_6017 = 6017
    const val UNKNOWN_SNOWSTORM_6018 = 6018
    const val UNKNOWN_SNOWSTORM_6019 = 6019
    const val UNKNOWN_SNOWSTORM_6020 = 6020
    const val UNKNOWN_SNOWSTORM_6021 = 6021
    const val UNKNOWN_SNOWSTORM_6022 = 6022
    const val UNKNOWN_SNOWSTORM_6023 = 6023
    const val UNKNOWN_SNOWSTORM_6024 = 6024
    const val UNKNOWN_SNOWSTORM_6025 = 6025
    const val SnowStormUserPickSnowball = 6026
    
}