package com.devband.tronwalletforandroid.di;

import com.devband.tronwalletforandroid.ui.about.AboutActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.overview.AccountVoteActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.overview.AccountVoteActivityModule;
import com.devband.tronwalletforandroid.ui.accountdetail.overview.OverviewModule;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.RepresentativeModule;
import com.devband.tronwalletforandroid.ui.accountdetail.transaction.TransactionModule;
import com.devband.tronwalletforandroid.ui.accountdetail.transfer.TransferModule;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.address.AddressActivityModule;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountActivity;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountActivityModule;
import com.devband.tronwalletforandroid.ui.blockdetail.BlockDetailActivity;
import com.devband.tronwalletforandroid.ui.blockdetail.fragment.BlockInfoModule;
import com.devband.tronwalletforandroid.ui.blockexplorer.BlockExplorerActivity;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivity;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivityModule;
import com.devband.tronwalletforandroid.ui.exchange.ExchangeActivity;
import com.devband.tronwalletforandroid.ui.exchange.ExchangeActivityModule;
import com.devband.tronwalletforandroid.ui.importkey.ImportPrivateKeyActivity;
import com.devband.tronwalletforandroid.ui.importkey.ImportPrivateKeyActivityModule;
import com.devband.tronwalletforandroid.ui.intro.IntroActivity;
import com.devband.tronwalletforandroid.ui.intro.IntroActivityModule;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivityModule;
import com.devband.tronwalletforandroid.ui.main.MainActivity;
import com.devband.tronwalletforandroid.ui.main.MainActivityModule;
import com.devband.tronwalletforandroid.ui.market.MarketActivity;
import com.devband.tronwalletforandroid.ui.market.MarketActivityModule;
import com.devband.tronwalletforandroid.ui.more.MoreActivity;
import com.devband.tronwalletforandroid.ui.more.MoreActivityModule;
import com.devband.tronwalletforandroid.ui.myaccount.MyAccountActivity;
import com.devband.tronwalletforandroid.ui.myaccount.MyAccountActivityModule;
import com.devband.tronwalletforandroid.ui.mytransfer.MyTransferActivityModule;
import com.devband.tronwalletforandroid.ui.mytransfer.TransferActivity;
import com.devband.tronwalletforandroid.ui.node.NodeActivity;
import com.devband.tronwalletforandroid.ui.node.NodeActivityModule;
import com.devband.tronwalletforandroid.ui.previewwallet.PreviewWalletActivity;
import com.devband.tronwalletforandroid.ui.previewwallet.PreviewWalletActivityModule;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;
import com.devband.tronwalletforandroid.ui.representative.RepresentativeActivity;
import com.devband.tronwalletforandroid.ui.representative.RepresentativeActivityModule;
import com.devband.tronwalletforandroid.ui.requestcoin.RequestCoinActivity;
import com.devband.tronwalletforandroid.ui.sendtrc10.SendTrc10Activity;
import com.devband.tronwalletforandroid.ui.sendtrc10.SendTrc10ActivityModule;
import com.devband.tronwalletforandroid.ui.sendtrc20.SendTrc20Activity;
import com.devband.tronwalletforandroid.ui.sendtrc20.SendTrc20ActivityModule;
import com.devband.tronwalletforandroid.ui.smartcontract.TestSmartContractActivity;
import com.devband.tronwalletforandroid.ui.token.TokenActivity;
import com.devband.tronwalletforandroid.ui.token.TokenActivityModule;
import com.devband.tronwalletforandroid.ui.token.TokenDetailActivity;
import com.devband.tronwalletforandroid.ui.vote.VoteActivity;
import com.devband.tronwalletforandroid.ui.vote.VoteActivityModule;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = AndroidInjectionModule.class)
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = { IntroActivityModule.class })
    abstract IntroActivity bindIntroActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { LoginActivityModule.class })
    abstract LoginActivity bindLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AboutActivity bindAboutActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract TestSmartContractActivity bindTestSmartContractActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { AddressActivityModule.class })
    abstract AddressActivity bindAddressActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { BackupAccountActivityModule.class })
    abstract BackupAccountActivity bindBackupAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { CreateWalletActivityModule.class })
    abstract CreateWalletActivity bindCreateWalletActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { ImportPrivateKeyActivityModule.class })
    abstract ImportPrivateKeyActivity bindImportPrivateKeyActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MainActivityModule.class })
    abstract MainActivity bindMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MarketActivityModule.class })
    abstract MarketActivity bindMarketActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MoreActivityModule.class })
    abstract MoreActivity bindMoreActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MyAccountActivityModule.class })
    abstract MyAccountActivity bindMyAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MyTransferActivityModule.class })
    abstract TransferActivity bindTransferActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { NodeActivityModule.class })
    abstract NodeActivity bindNodeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { RepresentativeActivityModule.class })
    abstract RepresentativeActivity bindRepresentativeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { SendTrc10ActivityModule.class })
    abstract SendTrc10Activity bindSendTokenActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { SendTrc20ActivityModule.class })
    abstract SendTrc20Activity bindSendTrc20Activity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { TokenActivityModule.class })
    abstract TokenActivity bindTokenActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { VoteActivityModule.class })
    abstract VoteActivity bindVoteActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            OverviewModule.class,
            TransactionModule.class,
            TransferModule.class,
            RepresentativeModule.class
    })
    abstract AccountDetailActivity bindAccountDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccountVoteActivityModule.class})
    abstract AccountVoteActivity bindAccountVoteActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            com.devband.tronwalletforandroid.ui.blockexplorer.overview.OverviewModule.class,
            com.devband.tronwalletforandroid.ui.blockexplorer.account.AccountModule.class,
            com.devband.tronwalletforandroid.ui.blockexplorer.block.BlockModule.class,
            com.devband.tronwalletforandroid.ui.blockexplorer.transaction.TransactionModule.class,
            com.devband.tronwalletforandroid.ui.blockexplorer.transfer.TransferModule.class
    })
    abstract BlockExplorerActivity bindBlockExplorerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            BlockInfoModule.class,
            TransactionModule.class,
            TransferModule.class
    })
    abstract BlockDetailActivity bindBlockDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract QrScanActivity bindQrScanActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RequestCoinActivity bindRequestCoinActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            com.devband.tronwalletforandroid.ui.token.overview.OverviewModule.class,
            com.devband.tronwalletforandroid.ui.token.holder.HolderModule.class,
            com.devband.tronwalletforandroid.ui.token.transfer.TransferModule.class
    })
    abstract TokenDetailActivity bindTokenDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { ExchangeActivityModule.class })
    abstract ExchangeActivity exchangeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { PreviewWalletActivityModule.class })
    abstract PreviewWalletActivity bindPreviewWalletActivity();
}
