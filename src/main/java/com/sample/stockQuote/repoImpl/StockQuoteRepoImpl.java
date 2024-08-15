package com.sample.stockQuote.repoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sample.stockQuote.model.QuoteModel;
import com.sample.stockQuote.model.StockQuoteModel;
import com.sample.stockQuote.repo.StockQuoteRepo;

@Repository("StockQuoteRepo")
public class StockQuoteRepoImpl implements StockQuoteRepo {

	private final DataSource dataSource;

	public StockQuoteRepoImpl(DataSource ds) {
		this.dataSource = ds;
	}
	
	@Value("${stock.data.fetch.delay.time:30}")
    private String delayTime;

	@Override
	public StockQuoteModel loadQuotesBySymbol(String symbol, Logger log) throws Exception {
		String sql = String.format(
				"select * from stock_quotes where symbol = ? and created_on >= now() - interval '%s'", delayTime);
		try (Connection con = dataSource.getConnection(); PreparedStatement pStmt = con.prepareStatement(sql)) {

			pStmt.setString(1, symbol);
			System.out.println("loadQuotesBySymbol() => sql Query : " + pStmt);
			log.info("loadQuotesBySymbol() => sql Query : " + pStmt);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					return setRowToModel(rs);
				}
			}
		}
		return null;
	}

	@Override
	public List<StockQuoteModel> getAllQuotesBySymbolsList(List<String> symbols, Logger log) throws Exception {
		if (symbols.isEmpty()) {
			return Collections.emptyList();
		}

		String sql = String.format(
				"select * from stock_quotes where symbol in (%s) and created_on >= now() - interval '%s'",
				symbols.stream().map(s -> "?").collect(Collectors.joining(",")), delayTime);

		try (Connection con = dataSource.getConnection(); PreparedStatement pStmt = con.prepareStatement(sql)) {

	        int index = 1;
	        for (String symbol : symbols) {
	            pStmt.setString(index++, symbol);
	        }

	        System.out.println("getAllQuotesBySymbolsList() => sql Query : " + pStmt);
			log.info("getAllQuotesBySymbolsList() => sql Query : " + pStmt);
			List<StockQuoteModel> stockQuoteList = new ArrayList<>();
			try (ResultSet rs = pStmt.executeQuery()) {
				while (rs.next()) {
					stockQuoteList.add(setRowToModel(rs));
				}
			}
			return stockQuoteList;
		}
	}

	private StockQuoteModel setRowToModel(ResultSet rs) throws SQLException {
		StockQuoteModel stockQuoteModel = new StockQuoteModel();
		QuoteModel quoteModel = new QuoteModel();

		quoteModel.setSymbol(rs.getString("symbol"));
		quoteModel.setChange(rs.getBigDecimal("change"));
		quoteModel.setHigh(rs.getBigDecimal("high"));
		quoteModel.setLatestTradingDay(rs.getString("latest_trading_day"));
		quoteModel.setLow(rs.getBigDecimal("low"));
		quoteModel.setOpen(rs.getBigDecimal("open"));
		quoteModel.setPercentageChange(rs.getBigDecimal("change_percent"));
		quoteModel.setPreviousClose(rs.getBigDecimal("previous_close"));
		quoteModel.setPrice(rs.getBigDecimal("price"));
		quoteModel.setVolume(rs.getBigDecimal("volume"));

		stockQuoteModel.setQuoteModel(quoteModel);
		return stockQuoteModel;
	}

	@Override
	public int insertQuoteData(StockQuoteModel stockQuoteModel, Logger log) {
		int i = 0;
		try {
			if (stockQuoteModel == null) {
				System.out.println("Received null model while insert call");
				log.error("insertQuoteData()--> Received null model while insert call");
				return i;
			} else if (stockQuoteModel.getQuoteModel().getSymbol() == null) {
				System.out.println("Received Symbol Var Null while insert call");
				log.error("insertQuoteData()--> Received Symbol Var Null while insert call");
				return i;
			}

			String sql = "insert into stock_quotes (symbol, open, high, low, price, volume, previous_close, change, change_percent, latest_trading_day, created_on) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ? , ?, current_timestamp) ";
			try (Connection con = dataSource.getConnection(); PreparedStatement pStmt = con.prepareStatement(sql)) {

				pStmt.setString(++i, stockQuoteModel.getQuoteModel().getSymbol());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getOpen());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getHigh());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getLow());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getPrice());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getVolume());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getPreviousClose());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getChange());
				pStmt.setBigDecimal(++i, stockQuoteModel.getQuoteModel().getPercentageChange());
				pStmt.setTimestamp(++i, stockQuoteModel.getQuoteModel()
						.getTimestamp(stockQuoteModel.getQuoteModel().getLatestTradingDay()));
				System.out.println("sql Query : " + pStmt);
				log.info("sql Insert Query for symbol: " + stockQuoteModel.getQuoteModel().getSymbol() + " : " + pStmt);
				return pStmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("insertQuoteData()--> Exception Occured while insert call: " + e.getMessage(), e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insertQuoteData()--> Exception Occured while insert call: " + e.getMessage(), e);
		}
		return i;
	}

}
