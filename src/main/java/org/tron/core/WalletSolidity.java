package org.tron.core;

import com.google.protobuf.ByteString;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tron.api.GrpcAPI.AccountList;
import org.tron.api.GrpcAPI.AssetIssueList;
import org.tron.api.GrpcAPI.NumberMessage;
import org.tron.api.GrpcAPI.TransactionList;
import org.tron.api.GrpcAPI.WitnessList;
import org.tron.common.utils.ByteArray;
import org.tron.core.db.api.StoreAPI;
import org.tron.core.exception.NonUniqueObjectException;
import org.tron.protos.Contract.AssetIssueContract;
import org.tron.protos.Protocol.Account;
import org.tron.protos.Protocol.Block;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.Witness;

@Slf4j
@Component
public class WalletSolidity {

  @Autowired
  private StoreAPI storeAPI;

  public Account getAccount(ByteString addressBs) {
    Account accountByAddress = null;
    try {
      accountByAddress = storeAPI.getAccountByAddress(addressBs.toStringUtf8());
    } catch (NonUniqueObjectException e) {
      e.printStackTrace();
    }
    return accountByAddress;
  }

  public AccountList getAccountList() {
    List<Account> accountAll = storeAPI.getAccountAll();
    AccountList accountList = AccountList.newBuilder().addAllAccounts(accountAll).build();
    return accountList;
  }

  public WitnessList getWitnessList() {
    List<Witness> witnessAll = storeAPI.getWitnessAll();
    WitnessList witnessList = WitnessList.newBuilder().addAllWitnesses(witnessAll).build();
    return witnessList;
  }

  public AssetIssueList getAssetIssueList() {
    List<AssetIssueContract> assetIssueAll = storeAPI.getAssetIssueAll();
    AssetIssueList assetIssueList =
        AssetIssueList.newBuilder().addAllAssetIssue(assetIssueAll).build();
    return assetIssueList;
  }

  public AssetIssueList getAssetIssueListByTimestamp(long timestamp) {
    List<AssetIssueContract> assetIssueAll = storeAPI.getAssetIssueByTime(timestamp);
    AssetIssueList assetIssueList =
        AssetIssueList.newBuilder().addAllAssetIssue(assetIssueAll).build();
    return assetIssueList;
  }

  public AssetIssueList getAssetIssueByAccount(ByteString address) {
    List<AssetIssueContract> assetIssueByOwnerAddress = storeAPI
        .getAssetIssueByOwnerAddress(address.toStringUtf8());
    AssetIssueList assetIssueList =
        AssetIssueList.newBuilder().addAllAssetIssue(assetIssueByOwnerAddress).build();
    return assetIssueList;
  }

  public AssetIssueContract getAssetIssueByName(ByteString name) {
    AssetIssueContract assetIssueByName = null;
    try {
      assetIssueByName = storeAPI.getAssetIssueByName(name.toStringUtf8());
    } catch (NonUniqueObjectException e) {
      e.printStackTrace();
    }
    return assetIssueByName;
  }

  public Block getNowBlock() {
    List<Block> latestBlocks = storeAPI.getLatestBlocks(1);
    if (CollectionUtils.isEmpty(latestBlocks)) {
      return null;
    }
    return latestBlocks.get(0);
  }

  public Block getBlockByNum(long num) {
    Block blockByNumber = null;
    try {
      blockByNumber = storeAPI.getBlockByNumber(num);
    } catch (NonUniqueObjectException e) {
      e.printStackTrace();
    }
    return blockByNumber;
  }

  public NumberMessage totalTransaction() {
    long transactionCount = storeAPI.getTransactionCount();
    return NumberMessage.newBuilder().setNum(transactionCount).build();
  }

  public Transaction getTransactionById(ByteString id) {
    try {
      Transaction transactionById = storeAPI.getTransactionById(id.toStringUtf8());
    } catch (NonUniqueObjectException e) {
      e.printStackTrace();
    }
    return null;
  }

  public TransactionList getTransactionsByTimestamp(long beginTime, long endTime) {
    List<Transaction> transactionsByTimestamp = storeAPI
        .getTransactionsByTimestamp(beginTime, endTime);
    TransactionList transactionList = TransactionList.newBuilder()
        .addAllTransaction(transactionsByTimestamp).build();
    return transactionList;
  }

  public TransactionList getTransactionsFromThis(ByteString thisAddress) {
    List<Transaction> transactionsFromThis = storeAPI
        .getTransactionsFromThis(ByteArray.toHexString(thisAddress.toByteArray()));
    TransactionList transactionList = TransactionList.newBuilder()
        .addAllTransaction(transactionsFromThis).build();
    return transactionList;
  }

  public TransactionList getTransactionsToThis(ByteString toAddress) {
    List<Transaction> transactionsToThis = storeAPI
        .getTransactionsToThis(ByteArray.toHexString(toAddress.toByteArray()));
    TransactionList transactionList = TransactionList.newBuilder()
        .addAllTransaction(transactionsToThis).build();
    return transactionList;
  }
}
