package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class LawActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tvLawL)
    TextView tvLawL;
    String law = "\n" +
            "     《软件使用协议及隐私政策》\n" +
            "\n" +
            "1.特别提示\n" +
            "1.1\n" +
            "本《软件许可及服务协议》（以下简称《协议》）是服务使用人（以下简称“用户”）与安徽好牛软件有限公司（以下简称“本公司”或者“我们”或者好牛）之间关于用户下载、安装、使用“鸿志出行”软件（ 包括但不限于PC版及移动电话、PDA版等各种无线手持终端版本的“鸿志出行”软件）；注册、使用、管理鸿志出行帐号；以及使用本公司提供的相关服务所订立的协议。\n" +
            "1.2\n" +
            "“鸿志出行”软件由安徽好牛软件有限公司研发，并同意按照本协议的规定及其不时发布的操作规则提供基于互联网以及移动网的相关服务（以下称“网络服务”）。为获得网络服务，用户应认真阅读、充分理解本《协议》中各条款，特别涉及免除或者限制本公司责任的免责条款，对用户的权利限制的条款、约定争议解决方式、司法管辖、法律适用的条款。请您审慎阅读并选择勾选接受或不接受本《协议》（无民事行为能力人、限制民事行为能力人应在法定监护人陪同下阅读）。除非您接受本《协议》所有条款，否则您无权下载、安装或使用本软件及其相关服务。您的下载、安装、使用、帐号获取和登录等行为将视为对本《协议》的接受，并同意接受本《协议》各项条款的约束。\n" +
            "1.3\n" +
            "用户注册成功后，本公司将给予每个用户一个用户帐号，该帐号归本公司所有，用户完成申请注册手续后，获得帐号的使用权。帐号使用权仅属于初始申请注册人，禁止赠与、借用、租用、转让或售卖。用户承担帐号与密码的保管责任，并就其帐号及密码项下之一切活动负全部责任。\n" +
            "2.知识产权声明\n" +
            "2.1\n" +
            "本“鸿志出行”软件是由本公司开发。“鸿志出行”软件的一切版权、商标权、专利权、商业秘密等知识产权，以及相关的所有信息内容，包括但不限于：文字表述及其组合、图标、图饰、图表、色彩、界面设计、版面框架、有关数据、印刷材料、或电子文档等均受中华人民共和国著作权法、商标法、专利法、反不正当竞争法和相应的国际条约以及其他知识产权法律法规的保护，除涉及第三方授权的软件或技术外，本公司享有上述知识产权。\n" +
            "2.2\n" +
            "未经本公司书面同意，用户不得为任何营利性或非营利性的目的自行实施、利用、转让或许可任何三方实施、利用、转让上述知识产权，本公司保留追究上述未经许可行为的权利。\n" +
            "3.授权范围\n" +
            "3.1\n" +
            "用户可以为非商业目的在单一台终端设备上安装、使用、显示、运行“鸿志出行”软件。用户不得为商业运营目的安装、使用、运行“鸿志出行”软件，不可以对本软件或者本软件运行过程中释放到任何计算机终端内存中的数据及本软件运行过程中客户端与服务器端的交互数据进行复制、更改、修改、挂接运行或创作任何衍生作品，形式包括但不限于使用插件、外挂或非经授权的第三方工具/服务接入本软件和相关系统。\n" +
            "3.2\n" +
            "用户不得未经本公司书面许可，将“鸿志出行”软件安装在未经本公司明示许可的其他终端设备上，包括但不限于机顶盒、无线上网机、游戏机、电视机等。\n" +
            "3.3\n" +
            "保留权利：未明示授权的其他一切权利仍归本公司所有，用户使用其他权利时须另外取得本公司的书面同意。\n" +
            "4.服务内容\n" +
            "4.1\n" +
            "鸿志出行”服务的具体内容由本公司根据实际情况提供，主要包括乘客和司机用户的注册登录、乘客发布打车、司机接收订单、司机抢单、实时聊天、订单完成后评价及举报投诉、查询打车和载客的历史记录等。\n" +
            "4.2\n" +
            "鸿志出行提供的部分网络服务为收费的网络服务，用户使用收费网络服务需要向本公司支付一定的费用。对于收费的网络服务，我们会尽量在用户使用之前给予用户明确的提示，只有用户根据提示确认其愿意支付相关费用，用户才能使用该等收费网络服务。如用户拒绝支付相关费用，则本公司有权不向用户提供该等收费网络服务。\n" +
            "4.3\n" +
            "用户理解，本公司仅提供相关的网络服务，除此之外与相关网络服务有关的设备（如个人电脑、手机、其他与接入互联网或移动网有关的装置）及第三方收取的相关费用（如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费）均应由用户自行负担。\n" +
            "5.服务变更、中断或终止\n" +
            "5.1\n" +
            "鉴于网络服务的特殊性，用户同意本公司有权随时变更、中断或终止部分或全部的网络服务（包括收费网络服务及免费网络服务）。如变更、中断或终止的网络服务属于免费网络服务，本公司无需通知用户，也无需对任何用户或任何第三方承担任何责任；如变更、中断或终止的网络服务属于收费网络服务，本公司应当在变更、中断或终止之前事先通知用户，并应向受影响的用户提供等值的替代性的收费网络服务，如用户不愿意接受替代性的收费网络服务，就该用户已经向本公司支付的服务费，本公司应当按照该用户实际使用相应收费网络服务的情况扣除相应服务费之后将剩余的服务费退还给该用户。\n" +
            "5.2\n" +
            "用户理解，本公司需要定期或不定期地对提供网络服务的平台（如互联网网站、移动网络等）或相关的设备进行检修或者维护，如因此类情况而造成收费网络服务在合理时间内的中断，本公司无需为此承担任何责任，且除特殊情况外应当事先进行通告。\n" +
            "5.3\n" +
            "如发生下列任何一种情形，本公司有权随时中断或终止向用户提供本协议项下的网络服务【该网络服务包括但不限于收费及免费网络服务（其中包括基于广告模式的免费网络服务）】而无需对用户或任何第三方承担任何责任： \n" +
            "5.3.1用户提供的个人资料不真实； \n" +
            "5.3.2用户违反本协议中规定的使用规则； \n" +
            "5.3.3用户在使用收费网络服务时未按规定向本公司支付相应的服务费；\n" +
            "5.4\n" +
            "如用户注册的免费网络服务的帐号在任何连续90日内未实际使用，或者用户注册的收费网络服务的帐号在其订购的收费网络服务的服务期满之后连续180日内未实际使用，则本公司有权删除该帐号并停止为该用户提供相关的网络服务。在执行账号删除之前，本公司会向该用户发送通知。\n" +
            "5.5\n" +
            "用户注册的免费帐号昵称和姓名如存在违反法律法规或国家政策要求，或侵犯任何第三方合法权益的情况，本公司有权禁止用户继续使用该帐号、昵称。\n" +
            "6.使用规则\n" +
            "6.1\n" +
            "司机用户在申请使用“鸿志出行”网络服务时，必须向本公司提供准确、真实的个人相关资料，且需要通过人工认证方能开始使用软件。如个人资料有任何变动，必须及时更新。更新后，本公司有权暂停该司机用户的使用权，同时需要人工再次认证方能继续使用软件。\n" +
            "6.2\n" +
            "用户不应将其帐号、密码转让或出借予他人使用。如用户发现其帐号遭他人非法使用，应立即通知本公司。因黑客行为或用户的保管疏忽导致帐号、密码遭他人非法使用，本公司不承担任何责任。\n" +
            "6.3\n" +
            "用户同意本公司有权在提供网络服务过程中以各种方式投放各种商业性广告或其他任何类型的商业信息，并且，用户同意接受本公司通过电子邮件或其他方式向用户发送商品促销或其他相关商业信息。\n" +
            "6.4\n" +
            "用户在使用“鸿志出行”服务过程中，必须遵循以下原则： \n" +
            "6.4.1遵守中国有关的法律和法规； \n" +
            "6.4.2遵守所有与网络服务有关的网络协议、规定和程序； \n" +
            "6.4.3不得为任何非法目的而使用网络服务系统； \n" +
            "6.4.4不得以任何形式使用“鸿志出行”服务侵犯本公司的商业利益，包括并不限于发布非经本公司许可的商业广告； \n" +
            "6.4.5不得利用“鸿志出行”网络服务系统进行任何可能对互联网或移动网正常运转造成不利影响的行为 \n" +
            "6.4.6不得利用本产品提供的网络服务上传、展示或传播任何虚假的、骚扰性的、中伤他人的、辱骂性的、恐吓性的、庸俗淫秽的或其他任何非法的信息资料； \n" +
            "6.4.7不得侵犯其他任何第三方的专利权、著作权、商标权、名誉权或其他任何合法权益； \n" +
            "6.4.8不得利用“鸿志出行”服务系统进行任何不利于本公司的行为。\n" +
            "本公司针对某些特定的“鸿志出行”网络服务的使用通过各种方式（包括但不限于网页公告、电子邮件、短信提醒等）作出的任何声明、通知、警示等内容视为本协议的一部分，用户如使用该网络服务，视为用户同意该等声明、通知、警示的内容。\n" +
            "6.5\n" +
            "本公司有权对用户使用“鸿志出行”网络服务【该网络服务包括但不限于收费及免费网络服务（其中包括基于广告模式的免费网络服务）】的情况进行审查和监督(包括但不限于对用户存储在本公司的内容进行审核)，如用户在使用网络服务时违反任何上述规定，本公司有权要求用户改正或直接采取一切必要的措施（包括但不限于更改或删除用户张贴的内容等、暂停或终止用户使用网络服务的权利）以减轻用户不当行为造成的影响。因用户自身行为需向第三人承担责任的，由用户自行承担，与本公司无关。\n" +
            "7.“鸿志出行”管理规定\n" +
            "7.1\n" +
            "用户注册“鸿志出行”账号，注册内容、抢单的各种信息，应当为信息的真实性负责。同时用户应当使用真实身份信息，不得以虚假、冒用的居民身份信息、企业注册信息、组织机构代码信息进行注册。\n" +
            "7.2\n" +
            "司机用户使用本软件期间，需诚信经营，不允许出现放鸽子行为\n" +
            "7.3\n" +
            "司机用户在使用本软件期间，以不真实订单、违规刷单（以本公司软件后台的记录为准）骗取本公司奖励,本公司有权从司机用户的注册账户中扣除等额于不真实订单、违规刷单骗取的金额并有权对司机用户进行处罚。司机用户以不真实订单、违规刷单骗取本公司奖励情节严重的本公司将保留追究法律责任的权利。\n" +
            "7.4\n" +
            "乘客提供证据证明其支付错误，并经本公司与司机用户沟通确认后，本公司可以协助司机用户直接从其账户中将乘客错付的车费返还给乘客。\n" +
            "7.5\n" +
            "“鸿志出行”将建立健全用户信息安全管理制度、落实技术安全防控措施。本公司将对用户使用“鸿志出行”网络服务过程中涉及的用户隐私内容加以保护。\n" +
            "\n" +
            "8.隐私保护\n" +
            "8.1\n" +
            "保护用户隐私是本公司的一项基本政策，本公司保证不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在“鸿志出行”的非公开内容，但下列情况除外： \n" +
            "8.1.1事先获得用户的明确授权； \n" +
            "8.1.2根据有关的法律法规要求； \n" +
            "8.1.3按照相关政府主管部门的要求； \n" +
            "8.1.4为维护社会公众的利益； \n" +
            "8.1.5为维护本公司的合法权益。\n" +
            "8.2\n" +
            "“鸿志出行”可能会与第三方合作向用户提供相关的网络服务，在此情况下，如该第三方同意承担与本公司同等的保护用户隐私的责任，则本公司有权将用户的注册资料等提供给该第三方。\n" +
            "8.3\n" +
            "在不透露单个用户隐私资料的前提下，本公司有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。 \n" +
            "本公司《鸿志出行对外隐私政策》作为本协议的有效组成部分，且您默许我公司不定时的更新隐私政策并接受。\n" +
            "9.免责声明\n" +
            "9.1\n" +
            "用户明确同意其使用本公司网络服务所存在的风险将完全由其自己承担；因其使用本公司网络服务而产生的一切后果也由其自己承担，本公司对用户不承担任何责任。\n" +
            "9.2\n" +
            "本公司对网络服务不作任何类型的担保，包括但不限于网络服务的及时性、安全性、准确性，对在任何情况下因使用或不能使用本网络服务所产生的直接、间接、偶然、特殊及后续的损害及风险，本公司不承担任何责任。\n" +
            "9.3\n" +
            "对于不可抗力、计算机病毒、黑客攻击、系统不稳定、用户所在位置、用户关机以及其他任何网络、技术、通信线路等原因造成的服务中断或不能满足用户要求的风险，由用户自行承担，本公司不承担任何责任。\n" +
            "9.4\n" +
            "用户同意，对于本公司向用户提供的下列产品或者服务的质量缺陷本身及其引发的任何损失，本公司无需承担任何责任： \n" +
            "9.4.1本公司向用户免费提供的各项网络服务； \n" +
            "9.4.2本公司向用户赠送的任何产品或者服务； \n" +
            "9.4.3本公司向收费网络服务用户附赠的各种产品或者服务。\n" +
            "9.5\n" +
            "用户（特别是司机用户）同意，“鸿志出行”所提供的功能受制于我国的交通法律法规和管理条例，即与本产品的功能和条例发生冲突时，应以各地的交通法律法规和管理条例为最高准则。任何通过“鸿志出行”服务直接或间接违反当地交通法律法规和管理条例的行为，该后果应由用户承担。如有举证需要，本公司可以向有关部门提供相关数据作为证据。\n" +
            "9.6\n" +
            "用户（特别是司机用户）理解安全驾驶的重要性，且保证在任何可能引起安全隐患的情况下均不得使用“鸿志出行”，并同意一切因使用“鸿志出行”服务而导致的安全隐患和因此产生的纠纷和交通事故，本公司概不负责赔偿。如有举证需要，本公司可以向有关部门提供相关数据作为证据。\n" +
            "10.违约赔偿\n" +
            "10.1\n" +
            "如因本公司违反有关法律、法规或本协议项下的任何条款而给用户造成损失，本公司同意承担由此造成的损害赔偿责任。\n" +
            "10.2\n" +
            "用户同意保障和维护本公司及其他用户的利益，如因用户违反有关法律、法规或本协议项下的任何条款而给本公司或任何其他第三人造成损失，用户同意承担由此造成的损害赔偿责任。\n" +
            "11.协议修改\n" +
            "11.1\n" +
            "本公司有权随时修改本协议的任何条款，一旦本协议的内容发生变动，本公司将会直接在本公司网站上公布修改之后的协议内容，该公布行为视为本公司已经通知用户修改内容。同时本公司也可通过其他适当方式向用户提示修改内容。\n" +
            "11.2\n" +
            "如果不同意本公司对本协议相关条款所做的修改，用户应当停止使用网络服务。如果用户继续使用网络服务，则视为用户接受本公司对本协议相关条款所做的修改。\n" +
            "12.通知送达\n" +
            "12.1\n" +
            "本协议项下本公司对于用户所有的通知均可通过网页公告、电子邮件、手机短信或常规的信件传送等方式进行；该等通知于发送之日视为已送达收件人。\n" +
            "12.2\n" +
            "用户对于本公司的通知应当通过本公司对外正式公布的通信地址、传真号码、电子邮件地址等联系信息进行送达。该等通知以本公司实际收到日为送达日。\n" +
            "13.法律管辖\n" +
            "13.1\n" +
            "本协议的订立、执行和解释及争议的解决均应适用中国法律并受中国法院管辖。\n" +
            "13.2\n" +
            "如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均可向本公司所在地的人民法院提起诉讼。\n" +
            "14.其他规定\n" +
            "\n" +
            "14.1\n" +
            "本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，除本协议规定的之外，未赋予本协议各方其他权利。\n" +
            "14.2\n" +
            "如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。\n" +
            "14.3\n" +
            "本协议中的标题仅为方便而设，在解释本协议时应被忽略。\n" +
            "\n" +
            "《鸿志出行对外隐私政策》\n" +
            "\n" +
            "鸿志出行非常重视对用户（以下简称“您”或“用户”）隐私的保护，为了给您提供更准确、更有个性化的服务，请您仔细阅读如下声明。当您访问鸿志出行网站或使用鸿志出行提供的服务前，您需要同意隐私政策中具体个人信息的收集、使用、公布和以其它形式使用和披露您的个人信息等内容。但鸿志出行会以高度的勤勉、审慎义务对待您的个人信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，鸿志出行不会将这些信息对外披露或向第三方提供。如果您不同意隐私政策中的任何内容，请立即停止使用或访问鸿志出行。本协议为鸿志出行用户许可协议不可分割的组成部分。\n" +
            "\n" +
            "一、鸿志出行隐私政策的适用范围\n" +
            "\n" +
            "1.1\n" +
            "适用范围\n" +
            "1.1.1\n" +
            "在您注册鸿志出行时，您根据鸿志出行要求提供的个人注册信息，包括但不限于姓名、地址、联系方式等（鸿志出行应法律法规要求需公示的企业名称及相关工商注册信息除外）； \n" +
            "1.1.2\n" +
            "在您使用鸿志出行网络服务，或访问鸿志出行平台网页时，鸿志出行自动接收并记录的您的浏览器和计算机上的信息，包括但不限于您的IP地址、浏览器的类型、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据； \n" +
            "1.1.3\n" +
            "鸿志出行通过合法途径从商业伙伴处取得的用户个人数据。\n" +
            "1.2\n" +
            "您了解并同意，以下信息不适用本隐私权政策：\n" +
            "1.2.1\n" +
            "您在使用鸿志出行平台提供的搜索服务时输入的关键字信息；\n" +
            "1.2.2\n" +
            "鸿志出行收集到的您在鸿志出行发布的有关信息数据，包括但不限于成交信息及评价详情；\n" +
            "1.2.3\n" +
            "违反法律规定或违反鸿志出行规则行为及鸿志出行已对您采取的措施。\n" +
            "\n" +
            "二、鸿志出行收集信息的使用\n" +
            "\n" +
            "2.1\n" +
            "鸿志出行利用从所有服务中收集的信息来提供、维护、保护和改进这些服务，同时开发新的服务为您带来更好的用户体验，并提高鸿志出行的总体服务品质。鸿志出行还会向您提供您感兴趣的信息，包括但不限于向您发出产品和服务信息，或者经您的事先同意，与鸿志出行合作伙伴共享信息以便他们向您发送有关其产品和服务的信息。\n" +
            "2.2\n" +
            "鸿志出行不会向任何无关第三方（即不包括鸿志出行的关联公司或合作公司）提供、出售、出租、分享或交易您的个人信息，除非事先得到您的许可，或该无关第三方和鸿志出行（含鸿志出行关联公司）单独或共同为您提供服务时，在该服务结束后，其将被禁止访问包括其以前能够访问的所有这些资料。\n" +
            "2.3\n" +
            "鸿志出行亦不允许任何第三方以任何手段收集、编辑、出售或者无偿传播您的个人信息。任何鸿志出行平台用户如从事上述活动，一经发现，鸿志出行有权立即终止与该用户的服务协议。\n" +
            "\n" +
            "三、您完全理解并不可撤销地、免费地授予鸿志出行及其关联公司下列权利\n" +
            "\n" +
            "3.1\n" +
            "鸿志出行关联公司或合作公司允许鸿志出行用户登录关联公司或合作公司并使用其服务，鸿志出行用户在关联公司或合作公司的任何行为均需遵守该等平台服务协议的约定、平台公布的规则以及有关正确使用平台服务的说明和操作指引。为了实现上述功能，您同意鸿志出行将您在鸿志出行的注册信息、交易/支付数据等信息和数据同步至关联公司或合作公司系统并允许其使用。\n" +
            "3.2\n" +
            "如您以鸿志出行关联公司或合作公司用户账号和密码登录鸿志出行，为了实现向您提供同等服务的功能，您同意鸿志出行将您在关联公司或合作公司账号项下的注册信息、交易/支付数据等信息和数据同步至鸿志出行系统并进行使用，并且您不会因此追究鸿志出行以及鸿志出行关联公司或合作公司的责任。\n" +
            "3.3\n" +
            "为确保交易安全，允许鸿志出行及其关联公司对用户信息进行数据分析，并允许鸿志出行及其关联公司对上述分析结果进行商业利用。\n" +
            "3.4\n" +
            "您在享受鸿志出行提供的各项服务的同时，授权并同意接受鸿志出行向您的电子邮件、手机、通信地址等发送商业信息，包括不限于最新的鸿志出行产品信息、促销信息等。若您选择不接受鸿志出行提供的各类信息服务，您可以按照鸿志出行网提供的相应设置拒绝该类信息服务。\n" +
            "3.5\n" +
            "用户在如下情况下，鸿志出行会遵照您的意愿或法律的规定披露您的个人信息，由此引发的问题将由您个人承担：\n" +
            "3.5.1\n" +
            "事先获得您的同意向第三方披露；\n" +
            "3.5.2\n" +
            "为提供您所要求的产品和服务，而必须与第三方分享您的个人信息；\n" +
            "3.5.3\n" +
            "根据有关的法律法规的规定，或者依据行政机关或司法机关的要求，向第三方或者行政、司法机构披露；\n" +
            "3.5.4\n" +
            "如您是适格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；\n" +
            "3.5.5\n" +
            "如您出现违反中国有关法律、法规或者鸿志出行服务协议或相关规则的情况，需要向第三方披露；\n" +
            "3.5.6\n" +
            "在鸿志出行上创建的某一交易中，如交易任何一方履行或部分履行了交易义务并提出信息披露请求的，鸿志出行有权决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决；\n" +
            "3.5.7\n" +
            "其他鸿志出行根据法律、法规或者鸿志出行政策认为合适的披露。\n" +
            "\n" +
            "四、信息存储和交换\n" +
            "\n" +
            "4.1\n" +
            "鸿志出行收集的有关您的信息和资料将保存在鸿志出行及（或）其关联公司的服务器上，这些信息和资料可能传送至您所在国家、地区或鸿志出行收集信息和资料所在地的境外并在境外被访问、存储和展示。\n" +
            "\n" +
            "五、Cookies的使用\n" +
            "\n" +
            "5.1\n" +
            "在您未拒绝接受cookies的情况下，鸿志出行会在您的计算机上设定或取用cookies，以便您能登录或使用依赖于cookies的鸿志出行服务或功能。鸿志出行使用cookies可为您提供更加周到的个性化服务，包括推广服务。\n" +
            "5.2\n" +
            "您有权选择接受或拒绝接受cookies。您可以通过修改浏览器设置的方式拒绝接受cookies。但如果您选择拒绝接受cookies，则您可能无法登录或使用依赖于cookies的鸿志出行服务或功能。\n" +
            "5.3\n" +
            "通过鸿志出行所设cookies所取得的有关信息，将适用本政策。\n" +
            "\n" +
            "六、鸿志出行对信息的维护与保护\n" +
            "\n" +
            "6.1\n" +
            "鸿志出行希望用户放心的使用鸿志出行的产品和服务，为此鸿志出行设置了安全程序保护您的信息不会被未经授权的人访问、窃取。但也请您妥善保管您的账户信息。鸿志出行将通过向其它服务器备份、对用户密码进行加密等安全措施确保您的信息不丢失，不被滥用和变造。尽管有前述安全措施，但同时也请您注意在信息网络上不存在绝对完善的安全措施。\n" +
            "6.2\n" +
            "在使用鸿志出行服务进行网上交易时，您不可避免的要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，请您立即联络鸿志出行客服，以便鸿志出行采取相应措施。\n" +
            "\n" +
            "七、合作伙伴\n" +
            "\n" +
            "7.1\n" +
            "鸿志出行选择有信誉的第三方公司或网站作为鸿志出行的合作伙伴为用户提供信息和服务，尽管鸿志出行只选择有信誉的公司或网站作为鸿志出行的合作伙伴，但是每个合作伙伴都会有与鸿志出行不同的隐私条款，一旦您通过点击进入了合作伙伴的网站，鸿志出行的隐私条款将不再生效，鸿志出行建议您查看该合作伙伴网站的隐私条款，并了解该合作伙伴对于收集、使用、披露您的个人信息的规定。\n" +
            "\n" +
            "八、关于隐私条款的变更\n" +
            "\n" +
            "8.1\n" +
            "鸿志出行将根据法律、法规或政策的变更和修改，或鸿志出行网站内容的变化和技术的更新，或其他鸿志出行认为合理的原因，对本隐私政策进行修改并以网站公告、用户通知等合适的形式告知用户，若您不接受修订后的条款的，应立即停止使用本服务，若您继续使用本服务的，视为接受修订后的所有条款。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);
        ButterKnife.bind(this);
        steepStatusBar();
        tvTitle.setText("法律条款");
        tvLawL.setText(law);
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
