class AddRankingColumnsToUser < ActiveRecord::Migration
  def up
  	add_column :users, :ranking, :integer
  end

  def down
  end
end
